package dev.juda.auth_service.messaging.handler;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import dev.juda.auth_service.messaging.dto.in.Command;
import dev.juda.auth_service.messaging.dto.in.CreateUserRequest;
import dev.juda.auth_service.messaging.dto.in.UpdateUserRequest;
import dev.juda.auth_service.messaging.dto.out.Reply;
import dev.juda.auth_service.service.interfaces.AuthService;
import dev.juda.auth_service.util.enums.CommandType;
import dev.juda.auth_service.util.enums.ReplyStatus;

@Configuration
public class UserCommandConsumer {

    private final UpdateUserRequest updateUserRequest;
    private final AuthService authService;

    public UserCommandConsumer(AuthService authService, UpdateUserRequest updateUserRequest) {
        this.authService = authService;
        this.updateUserRequest = updateUserRequest;
    }

    @Bean
    public Function<Message<Command<CreateUserRequest>>, Message<Reply<Object>>> handleCommands(){
        return msg -> {
            String correlationId = msg.getHeaders().get("correlationId").toString();
            if (correlationId == null || correlationId.isBlank()) {
                return MessageBuilder.withPayload(new Reply<>(ReplyStatus.ERROR, "Missing correlationId",null))
                .build();
            }

            Command<?> cmd = msg.getPayload();

            Reply<Object> reply = switch(cmd.type()) {
                case CommandType.CREATE -> {
                    if (cmd.body() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "Create Empty body", null);
                    }

                    CreateUserRequest createUserRequest = (CreateUserRequest) cmd.body();

                    yield new Reply<>(ReplyStatus.SUCCESS, "User created", authService.create(createUserRequest));
                }

                case CommandType.UPDATE -> {
                    if (cmd.body() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "Update Empty body", null);
                    }

                    if (cmd.id() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "User Keycloak Id is null", null);
                    }

                    UpdateUserRequest updateUserRequest = (UpdateUserRequest) cmd.body();

                    authService.update(cmd.id(), updateUserRequest);

                    yield new Reply<>(ReplyStatus.SUCCESS, "User updated", null);
                }

                default -> {
                    yield new Reply<>(ReplyStatus.ERROR, "Unknown command type", null);
                }
            };

            return MessageBuilder.withPayload(reply).setHeader("correlationId", correlationId).build();
        };
    }

}
