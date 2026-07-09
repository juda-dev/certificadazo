package dev.juda.templates_service.template.messaging.handler;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import dev.juda.templates_service.template.messaging.dto.in.Command;
import dev.juda.templates_service.template.messaging.dto.in.TemplateAiResponse;
import dev.juda.templates_service.template.messaging.dto.out.Reply;
import dev.juda.templates_service.template.service.interfaces.TemplateService;
import dev.juda.templates_service.template.util.enums.CommandType;
import dev.juda.templates_service.template.util.enums.ReplyStatus;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class AiCommandConsumer {

    private final TemplateService templateService;
    private final ObjectMapper mapper;

    public AiCommandConsumer(TemplateService templateService, ObjectMapper mapper) {
        this.templateService = templateService;
        this.mapper = mapper;
    }

    @Bean
    Function<Message<Command<Object>>, Message<Reply<Object>>> handleCommands() {
        return msg -> {
            String correlationId = msg.getHeaders().get("correlationId").toString();
            if (correlationId == null || correlationId.isBlank()) {
                return MessageBuilder.withPayload(new Reply<>(ReplyStatus.ERROR, "Missing correlationId", null))
                        .build();
            }

            Command<?> cmd = msg.getPayload();

            Reply<Object> reply = switch (cmd.type()) {
                case CommandType.CREATE -> {
                    if (cmd.body() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "Create Empty body", null);
                    }

                    TemplateAiResponse templateAiResponse = mapper.convertValue(cmd.body(), TemplateAiResponse.class);

                    yield new Reply<>(ReplyStatus.SUCCESS, "Template created",
                            templateService.create(templateAiResponse));
                }

                default -> {
                    yield new Reply<>(ReplyStatus.ERROR, "Unknown command type", null);
                }
            };

            return MessageBuilder.withPayload(reply).setHeader("correlationId", correlationId).build();
        };
    }

}
