package dev.juda.ai_service.information.service.implementation;

import dev.juda.ai_service.information.presentation.dto.in.InformationResponse;
import dev.juda.ai_service.information.presentation.dto.out.InformationAiResponse;
import dev.juda.ai_service.information.presentation.dto.request.InformationRequest;
import dev.juda.ai_service.information.presentation.dto.response.InformationAiWithoutIdResponse;
import dev.juda.ai_service.information.service.interfaces.InformationAiService;
import dev.juda.ai_service.shared.messaging.app.ReplyInbox;
import dev.juda.ai_service.shared.messaging.dto.in.Reply;
import dev.juda.ai_service.shared.messaging.dto.out.Command;
import dev.juda.ai_service.shared.service.interfaces.CsvConverter;
import dev.juda.ai_service.shared.service.interfaces.FileValidator;
import dev.juda.ai_service.shared.util.enums.CommandType;
import dev.juda.ai_service.shared.util.enums.SupportedFileType;
import dev.juda.ai_service.template.service.exception.CommandNotSentException;
import dev.juda.ai_service.template.service.exception.TimeoutCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class InformationAiServiceImpl implements InformationAiService {

    private final RestClient usersRestClient;
    private final RestClient templatesRestClient;
    private final FileValidator fileValidator;
    private final ChatClient deepseekChatClient;
    private final CsvConverter csvConverter;
    private final ObjectMapper mapper;
    private final StreamBridge streamBridge;
    private final ReplyInbox replyInbox;
    private static final Logger LOG = LoggerFactory.getLogger(InformationAiServiceImpl.class);

    public InformationAiServiceImpl(@Qualifier("templates") RestClient templatesRestClient, FileValidator fileValidator,
            @Qualifier("deepSeekChatClient") ChatClient deepseekChatClient, CsvConverter csvConverter,
            ObjectMapper mapper, @Qualifier("users") RestClient usersRestClient, ReplyInbox replyInbox,
            StreamBridge streamBridge) {
        this.usersRestClient = usersRestClient;
        this.templatesRestClient = templatesRestClient;
        this.fileValidator = fileValidator;
        this.deepseekChatClient = deepseekChatClient;
        this.csvConverter = csvConverter;
        this.mapper = mapper;
        this.streamBridge = streamBridge;
        this.replyInbox = replyInbox;
    }

    @Override
    public Set<InformationResponse> create(MultipartFile file, InformationRequest req) {
        LOG.trace("Starting creation of information for the template {}", req.templateId());
        String fileType = fileValidator.validate(file, SupportedFileType.CSV);

        String csvText = csvConverter.toPlainText(file);

        LOG.trace("Obtaining the fields from the template service");
        Set<String> fields = templatesRestClient.get()
                .uri("/fields/{id}", req.templateId())
                .retrieve()
                .body(new ParameterizedTypeReference<Set<String>>() {
                });
        LOG.debug("Fields obtained {}",  fields);

        String dataStructure = mapper.writeValueAsString(fields);

        LOG.trace("Initiating a DeepSeek API request");
        var aiResponse = deepseekChatClient
                .prompt()
                .user(u -> u.text("""
                        templateId: {templateId};
                        CSV file in plain text: {csvText};
                        JSON object representing the exact structure that the "data" object must have: {dataStructure}
                        """)
                        .param("templateId", req.templateId())
                        .param("csvText", csvText)
                        .param("dataStructure", dataStructure))
                .call()
                .entity(new ParameterizedTypeReference<Set<InformationAiWithoutIdResponse>>() {
                });

        Set<InformationAiResponse> finalResponse = aiResponse.stream()
                .map(ai -> {

                    try {
                        UUID userId = null;

                        if (!ai.documentId().isBlank()) {
                            LOG.trace("Receiving user based on their documentId from the user service");
                            userId = usersRestClient.get()
                                    .uri("/find-id/document-id/{documentId}", ai.documentId())
                                    .retrieve()
                                    .body(UUID.class);
                        } else if (!ai.email().isBlank()) {
                            LOG.trace("Receiving user based on their email from the user service");
                            userId = usersRestClient.get()
                                    .uri("/find-id/email/{email}", ai.email())
                                    .retrieve()
                                    .body(UUID.class);
                        }

                        return new InformationAiResponse(userId, ai.templateId(), ai.data());
                    } catch (Exception e) {
                        LOG.error("Error trying to receive a response from the DeepSeek API: {}", e.getMessage(), e);
                        return null;
                    }

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        LOG.trace("A command is sent to the template service");
        var cmd = new Command<>(CommandType.CREATE_INFORMATION, null, finalResponse);
        Reply<?> reply = getReply(cmd, "CREATE");

        return mapper.convertValue(reply.body(), new TypeReference<Set<InformationResponse>>() {
        });
    }

    private Reply<?> getReply(Command<?> cmd, String methodName) {
        LOG.trace("Starting process to receive a response from the template service");
        String correlationId = UUID.randomUUID().toString();
        var future = replyInbox.register(correlationId);

        var msg = MessageBuilder
                .withPayload(cmd)
                .setHeader("correlationId", correlationId)
                .build();

        boolean sent = this.streamBridge.send("commands-out-0", msg);

        if (!sent) {
            LOG.error("Error trying to send command to template service");
            throw new CommandNotSentException();
        }

        Reply<?> reply;

        try {
            reply = (Reply<?>) future.get(Duration.ofSeconds(15).toMillis(), TimeUnit.MILLISECONDS);
            LOG.info("Response received from the template service");
        } catch (InterruptedException | ExecutionException | TimeoutCommandException | TimeoutException e) {
            LOG.error("Error trying to send command to template service", e);
            throw new TimeoutCommandException("ai-service_" + methodName);
        }

        return reply;
    }
}
