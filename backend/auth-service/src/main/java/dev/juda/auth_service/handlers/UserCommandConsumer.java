package dev.juda.auth_service.handlers;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import dev.juda.auth_service.models.dto.messaging.Command;
import dev.juda.auth_service.models.dto.messaging.CreateUserRequest;
import dev.juda.auth_service.models.dto.messaging.Reply;
import dev.juda.auth_service.models.enums.CommandType;
import dev.juda.auth_service.models.enums.ReplyStatus;
import dev.juda.auth_service.services.AuthService;

@Configuration
public class UserCommandConsumer {

    private final AuthService authService;

    public UserCommandConsumer(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public Function<Message<Command<CreateUserRequest>>, Message<Reply<Object>>> handleCommands(){
        return msg -> {
            String correlationId = msg.getHeaders().get("correlationId").toString();
            if (correlationId == null || correlationId.isBlank()) {
                return MessageBuilder.withPayload(new Reply<>(ReplyStatus.ERROR, "Missing correlationId",null))
                .build();
            }

            Command<CreateUserRequest> cmd = msg.getPayload();

            Reply<Object> reply = switch(cmd.type()) {
                case CommandType.CREATE -> {
                    if (cmd.body() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "Create Empty body", null);
                    }

                    CreateUserRequest createUserRequest = cmd.body();

                    yield new Reply<>(ReplyStatus.SUCCESS, "User created", authService.create(createUserRequest));
                }

                default -> {
                    yield new Reply<>(ReplyStatus.ERROR, "Unknown command type", null);
                }
            };

            return MessageBuilder.withPayload(reply).setHeader("correlationId", correlationId).build();
        };
    }

}
