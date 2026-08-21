package dev.juda.ai_service.template.service.implementation;

import dev.juda.ai_service.shared.messaging.app.ReplyInbox;
import dev.juda.ai_service.shared.messaging.dto.in.Reply;
import dev.juda.ai_service.shared.messaging.dto.out.Command;
import dev.juda.ai_service.shared.service.interfaces.FileValidator;
import dev.juda.ai_service.shared.util.enums.CommandType;
import dev.juda.ai_service.shared.util.enums.SupportedFileType;
import dev.juda.ai_service.template.presentation.dto.in.TemplateResponse;
import dev.juda.ai_service.template.presentation.dto.out.TemplateAiResponse;
import dev.juda.ai_service.template.presentation.dto.request.TemplateRequest;
import dev.juda.ai_service.template.service.exception.CommandNotSentException;
import dev.juda.ai_service.template.service.exception.DepartmentNotFoundException;
import dev.juda.ai_service.template.service.exception.InvalidFileTypeException;
import dev.juda.ai_service.template.service.exception.TimeoutCommandException;
import dev.juda.ai_service.template.service.interfaces.FileStorageService;
import dev.juda.ai_service.template.service.interfaces.PdfConverter;
import dev.juda.ai_service.template.service.interfaces.TemplateAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class TemplateAiServiceImpl implements TemplateAiService {
    private final RestClient restClient;
    private final ChatClient qwenChatClient;
    private final FileStorageService fileStorageService;
    private final FileValidator fileValidator;
    private final PdfConverter pdfConverter;
    private final StreamBridge streamBridge;
    private final ReplyInbox replyInbox;
    private final ObjectMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger(TemplateAiServiceImpl.class);

    public TemplateAiServiceImpl(@Qualifier("departments") RestClient restClient,
            @Qualifier("qwenChatClient") ChatClient qwenChatClient,
            FileStorageService fileStorageService, FileValidator fileValidator, PdfConverter pdfConverter,
            ObjectMapper mapper, ReplyInbox replyInbox, StreamBridge streamBridge) {
        this.restClient = restClient;
        this.qwenChatClient = qwenChatClient;
        this.fileStorageService = fileStorageService;
        this.fileValidator = fileValidator;
        this.pdfConverter = pdfConverter;
        this.streamBridge = streamBridge;
        this.replyInbox = replyInbox;
        this.mapper = mapper;
    }

    @Override
    public TemplateResponse createTemplate(MultipartFile file, TemplateRequest req) {
        LOG.trace("Starting template creation");

        LOG.trace("Checking if there is a department with id {}", req.departmentId());
        Boolean existsDepartmentById = restClient.get()
                .uri("/exists/{id}", req.departmentId())
                .retrieve()
                .body(Boolean.class);

        if (!existsDepartmentById) {
            LOG.warn("Department not found with id {}", req.departmentId());
            throw new DepartmentNotFoundException();
        }

        String previewSrc = fileStorageService.savePreview(file);

        String fileType = fileValidator.validate(file, SupportedFileType.IMAGE_PDF);

        byte[] imageBytes;

        switch (fileType) {
            case "application/pdf" -> {
                try {
                    imageBytes = pdfConverter.pdfToImage(file.getBytes());
                } catch (IOException e) {
                    LOG.error("Error trying to convert PDF to image");
                    throw new InvalidFileTypeException();
                }
            }

            default -> {
                try {
                    imageBytes = file.getBytes();
                } catch (IOException e) {
                    LOG.error("Error trying to convert PDF to image");
                    throw new InvalidFileTypeException();
                }
            }
        }

        var imageResource = new ByteArrayResource(imageBytes);

        LOG.trace("Initiating a Qwen API request to create a template");
        var templateAiResponse = qwenChatClient.prompt()
                .user(u -> u
                        .text("departmentId=" + req.departmentId() + " previewSrc=" + previewSrc)
                        .media(MediaType.IMAGE_PNG, imageResource))
                .call()
                .entity(TemplateAiResponse.class);

        var cmd = new Command<>(CommandType.CREATE_TEMPLATE, null, templateAiResponse);

        Reply<?> reply = getReply(cmd, "CREATE");

        LOG.info("Template created");
        return mapper.convertValue(reply.body(), TemplateResponse.class);

    }

    private Reply<?> getReply(Command<?> cmd, String methodName) {
        String correlationId = UUID.randomUUID().toString();
        var future = replyInbox.register(correlationId);

        LOG.trace("Send command to the template service for creation");
        var msg = MessageBuilder
                .withPayload(cmd)
                .setHeader("correlationId", correlationId)
                .build();

        boolean sent = this.streamBridge.send("commands-out-0", msg);

        if (!sent) {
            LOG.error("Error sending command to template service");
            throw new CommandNotSentException();
        }

        Reply<?> reply;

        try {
            reply = (Reply<?>) future.get(Duration.ofSeconds(5).toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutCommandException | TimeoutException e) {
            LOG.error("Timeout while trying to receive a response from the template service for creating a");
            throw new TimeoutCommandException("ai-service_" + methodName);
        }

        LOG.info("Response received from the template service");
        return reply;
    }

}
