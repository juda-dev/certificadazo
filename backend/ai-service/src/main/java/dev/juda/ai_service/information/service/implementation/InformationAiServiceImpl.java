package dev.juda.ai_service.information.service.implementation;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

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
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

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
        String fileType = fileValidator.validate(file, SupportedFileType.CSV);

        String csvText = csvConverter.toPlainText(file);

        Set<String> fields = templatesRestClient.get()
                .uri("/fields/{id}", req.templateId())
                .retrieve()
                .body(new ParameterizedTypeReference<Set<String>>() {
                });

        String dataStructure = mapper.writeValueAsString(fields);

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
                            userId = usersRestClient.get()
                                    .uri("/find-id/document-id/{documentId}", ai.documentId())
                                    .retrieve()
                                    .body(UUID.class);
                        } else if (!ai.email().isBlank()) {
                            userId = usersRestClient.get()
                                    .uri("/find-id/email/{email}", ai.email())
                                    .retrieve()
                                    .body(UUID.class);
                        }

                        return new InformationAiResponse(userId, ai.templateId(), ai.data());
                    } catch (Exception e) {
                        return null;
                    }

                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        var cmd = new Command<>(CommandType.CREATE_INFORMATION, null, finalResponse);
        Reply<?> reply = getReply(cmd, "CREATE");

        return mapper.convertValue(reply.body(), new TypeReference<Set<InformationResponse>>() {
        });
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
