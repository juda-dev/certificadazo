package dev.juda.users_service.services.impl;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.juda.users_service.exceptions.CommandNotSentException;
import dev.juda.users_service.exceptions.TimeoutCommandException;
import dev.juda.users_service.mappers.UserMapper;
import dev.juda.users_service.messaging.ReplyInbox;
import dev.juda.users_service.models.dto.messaging.Command;
import dev.juda.users_service.models.dto.messaging.CreateUserReply;
import dev.juda.users_service.models.dto.messaging.Reply;
import dev.juda.users_service.models.dto.request.CreateUserRequest;
import dev.juda.users_service.models.dto.response.CreateUserResponse;
import dev.juda.users_service.models.entities.UserEntity;
import dev.juda.users_service.models.enums.CommandType;
import dev.juda.users_service.repositories.UserRepository;
import dev.juda.users_service.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StreamBridge streamBridge;
    private final ReplyInbox replyInbox;

    public UserServiceImpl(UserRepository userRepository, StreamBridge streamBridge, ReplyInbox replyInbox) {
        this.userRepository = userRepository;
        this.streamBridge = streamBridge;
        this.replyInbox = replyInbox;
    }

    @Override
    @Transactional
    public CreateUserResponse create(CreateUserRequest req) {
        var cmd = new Command<>(CommandType.CREATE, null, req);

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

        Reply<CreateUserReply> reply;

        try {
            reply = (Reply<CreateUserReply>) future.get(Duration.ofSeconds(5).toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutCommandException | TimeoutException e) {
            throw new TimeoutCommandException("Timeout waiting for response from KeyCloak service");
        }

        UserEntity user = new UserEntity();
        user.setDocumentId(req.documentId());
        user.setName(req.name());
        user.setKeycloackId(reply.body().keycloakId());

        return UserMapper.toCreateUserResponse(userRepository.save(user));
    }

}
