package dev.juda.ai_service.template.service.implementation;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.shared.messaging.app.ReplyInbox;
import dev.juda.ai_service.shared.messaging.dto.in.Reply;
import dev.juda.ai_service.shared.messaging.dto.out.Command;
import dev.juda.ai_service.shared.service.interfaces.FileValidator;
import dev.juda.ai_service.shared.util.enums.CommandType;
import dev.juda.ai_service.shared.util.enums.SupportedFileType;
import dev.juda.ai_service.template.presentation.dto.in.TemplateResponse;
import dev.juda.ai_service.template.presentation.dto.out.TemplateAiResponse;
import dev.juda.ai_service.template.presentation.dto.request.TemplateRequest;
import dev.juda.ai_service.template.presentation.exception.CommandNotSentException;
import dev.juda.ai_service.template.presentation.exception.DepartmentNotFoundException;
import dev.juda.ai_service.template.presentation.exception.InvalidFileTypeException;
import dev.juda.ai_service.template.presentation.exception.TimeoutCommandException;
import dev.juda.ai_service.template.service.interfaces.TemplateAiService;
import dev.juda.ai_service.template.service.interfaces.FileStorageService;
import dev.juda.ai_service.template.service.interfaces.PdfConverter;
import tools.jackson.databind.ObjectMapper;

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
        Boolean existsDepartmentById = restClient.get()
                .uri("/exists/{id}", req.departmentId())
                .retrieve()
                .body(Boolean.class);

        if (!existsDepartmentById)
            throw new DepartmentNotFoundException();

        String previewSrc = fileStorageService.savePreview(file);

        String fileType = fileValidator.validate(file, SupportedFileType.IMAGE_PDF);

        byte[] imageBytes;

        switch (fileType) {
            case "application/pdf" -> {
                try {
                    imageBytes = pdfConverter.pdfToImage(file.getBytes());
                } catch (IOException e) {
                    throw new InvalidFileTypeException();
                }
            }

            default -> {
                try {
                    imageBytes = file.getBytes();
                } catch (IOException e) {
                    throw new InvalidFileTypeException();
                }
            }
        }

        var imageResource = new ByteArrayResource(imageBytes);

        var templateAiResponse = qwenChatClient.prompt()
                .user(u -> u
                        .text("departmentId=" + req.departmentId() + " previewSrc=" + previewSrc)
                        .media(MediaType.IMAGE_PNG, imageResource))
                .call()
                .entity(TemplateAiResponse.class);

        var cmd = new Command<>(CommandType.CREATE, null, templateAiResponse);

        Reply<?> reply = getReply(cmd, "CREATE");

        return mapper.convertValue(reply.body(), TemplateResponse.class);

    }

    private Reply<?> getReply(Command<?> cmd, String methodName) {
        String correlationId = UUID.randomUUID().toString();
        var future = replyInbox.register(correlationId);

        var msg = MessageBuilder
                .withPayload(cmd)
                .setHeader("correlationId", correlationId)
                .build();

        boolean sent = this.streamBridge.send("commands-out-0", msg);

        if (!sent) {
            throw new CommandNotSentException();
        }

        Reply<?> reply;

        try {
            reply = (Reply<?>) future.get(Duration.ofSeconds(5).toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutCommandException | TimeoutException e) {
            throw new TimeoutCommandException("ai-service_" + methodName);
        }

        return reply;
    }

}
