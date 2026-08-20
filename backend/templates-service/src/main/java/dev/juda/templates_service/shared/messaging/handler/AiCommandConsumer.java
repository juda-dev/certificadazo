package dev.juda.templates_service.shared.messaging.handler;

import dev.juda.templates_service.information.presentation.dto.in.InformationAiResponse;
import dev.juda.templates_service.information.service.interfaces.InformationService;
import dev.juda.templates_service.shared.messaging.dto.in.Command;
import dev.juda.templates_service.shared.messaging.dto.in.TemplateAiResponse;
import dev.juda.templates_service.shared.messaging.dto.out.Reply;
import dev.juda.templates_service.shared.util.enums.CommandType;
import dev.juda.templates_service.shared.util.enums.ReplyStatus;
import dev.juda.templates_service.template.service.interfaces.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;
import java.util.function.Function;

@Configuration
public class AiCommandConsumer {

    private final InformationService informationService;
    private final TemplateService templateService;
    private final ObjectMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger(AiCommandConsumer.class);

    public AiCommandConsumer(TemplateService templateService, ObjectMapper mapper,
            InformationService informationService) {
        this.templateService = templateService;
        this.mapper = mapper;
        this.informationService = informationService;
    }

    @Bean
    Function<Message<Command<Object>>, Message<Reply<Object>>> handleCommands() {
        return msg -> {
            LOG.trace("Initiating processing of command received from AI service");
            String correlationId = msg.getHeaders().get("correlationId").toString();
            if (correlationId == null || correlationId.isBlank()) {
                LOG.error("Missing correlationId");
                return MessageBuilder.withPayload(new Reply<>(ReplyStatus.ERROR, "Missing correlationId", null))
                        .build();
            }

            Command<?> cmd = msg.getPayload();

            Reply<Object> reply = switch (cmd.type()) {
                case CommandType.CREATE_TEMPLATE -> {
                    LOG.trace("Starting template creation");
                    if (cmd.body() == null) {
                        LOG.error("Missing body");
                        yield new Reply<>(ReplyStatus.ERROR, "Create Empty body", null);
                    }

                    TemplateAiResponse templateAiResponse = mapper.convertValue(cmd.body(), TemplateAiResponse.class);

                    yield new Reply<>(ReplyStatus.SUCCESS, "Template created",
                            templateService.create(templateAiResponse));
                }

                case CommandType.CREATE_INFORMATION -> {
                    LOG.trace("Starting information creation");
                    if (cmd.body() == null) {
                        LOG.error("Missing body");
                        yield new Reply<>(ReplyStatus.ERROR, "Create Empty body", null);
                    }

                    Set<InformationAiResponse> informationAiResponse = mapper.convertValue(cmd.body(),
                            new TypeReference<Set<InformationAiResponse>>() {
                            });

                    yield new Reply<>(ReplyStatus.SUCCESS, "Information created",
                            informationService.create(informationAiResponse));
                }
            };

            return MessageBuilder.withPayload(reply).setHeader("correlationId", correlationId).build();
        };
    }

}
