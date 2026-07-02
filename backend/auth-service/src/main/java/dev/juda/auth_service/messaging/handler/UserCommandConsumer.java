package dev.juda.auth_service.messaging.handler;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import tools.jackson.databind.ObjectMapper;

import dev.juda.auth_service.messaging.dto.in.Command;
import dev.juda.auth_service.messaging.dto.in.CreateUserRequest;
import dev.juda.auth_service.messaging.dto.in.PasswordChangeRequest;
import dev.juda.auth_service.messaging.dto.in.UpdateUserRequest;
import dev.juda.auth_service.messaging.dto.out.Reply;
import dev.juda.auth_service.service.interfaces.AuthService;
import dev.juda.auth_service.util.enums.CommandType;
import dev.juda.auth_service.util.enums.ReplyStatus;

@Configuration
public class UserCommandConsumer {

    private final AuthService authService;
    private final ObjectMapper mapper;

    public UserCommandConsumer(AuthService authService, ObjectMapper mapper) {
        this.authService = authService;
        this.mapper = mapper;
    }

    @Bean
    public Function<Message<Command<Object>>, Message<Reply<Object>>> handleCommands() {
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

                    CreateUserRequest createUserRequest = mapper.convertValue(cmd.body(), CreateUserRequest.class);

                    yield new Reply<>(ReplyStatus.SUCCESS, "User created", authService.create(createUserRequest));
                }

                case CommandType.UPDATE -> {
                    if (cmd.body() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "Update Empty body", null);
                    }

                    if (cmd.id() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "User Keycloak Id is null", null);
                    }

                    UpdateUserRequest updateUserRequest = mapper.convertValue(cmd.body(), UpdateUserRequest.class);

                    authService.update(cmd.id(), updateUserRequest);

                    yield new Reply<>(ReplyStatus.SUCCESS, "User updated", null);
                }

                case CommandType.PASSWORD_UPDATE -> {
                    if (cmd.body() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "Update Empty body", null);
                    }

                    if (cmd.id() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "User Keycloak Id is null", null);
                    }

                    PasswordChangeRequest passwordChangeRequest = mapper.convertValue(cmd.body(),
                            PasswordChangeRequest.class);

                    Reply<?> rep = authService.updatePassword(cmd.id(), passwordChangeRequest);

                    yield new Reply<>(rep.status(), rep.message(), null);
                }

                case CommandType.DELETE -> {
                    if (cmd.id() == null) {
                        yield new Reply<>(ReplyStatus.ERROR, "User Keycloak Id is null", null);
                    }

                    authService.delete(cmd.id());

                    yield new Reply<>(ReplyStatus.SUCCESS, null, null);
                }

                default -> {
                    yield new Reply<>(ReplyStatus.ERROR, "Unknown command type", null);
                }
            };

            return MessageBuilder.withPayload(reply).setHeader("correlationId", correlationId).build();
        };
    }

}
