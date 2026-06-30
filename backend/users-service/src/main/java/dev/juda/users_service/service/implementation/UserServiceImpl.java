package dev.juda.users_service.service.implementation;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.juda.users_service.messaging.app.ReplyInbox;
import dev.juda.users_service.messaging.dto.in.CreateUserReply;
import dev.juda.users_service.messaging.dto.in.Reply;
import dev.juda.users_service.messaging.dto.out.Command;
import dev.juda.users_service.persistence.entity.UserEntity;
import dev.juda.users_service.persistence.repository.UserRepository;
import dev.juda.users_service.presentation.dto.request.CreateUserRequest;
import dev.juda.users_service.presentation.dto.response.CreateUserResponse;
import dev.juda.users_service.service.exception.CommandNotSentException;
import dev.juda.users_service.service.exception.ExistingUserException;
import dev.juda.users_service.service.exception.TimeoutCommandException;
import dev.juda.users_service.service.interfaces.UserService;
import dev.juda.users_service.util.enums.CommandType;
import dev.juda.users_service.util.mapper.UserMapper;
import tools.jackson.databind.ObjectMapper;

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

        if (userRepository.existsByEmail(req.email())) {
            throw new ExistingUserException("email address.");
        }

        if (userRepository.existsByDocumentId(req.documentId())) {
            throw new ExistingUserException("document id.");
        }

        var cmd = new Command<>(CommandType.CREATE, null, req);

        String correlationId = UUID.randomUUID().toString();
        var future = replyInbox.register(correlationId);

        var msg = MessageBuilder
                .withPayload(cmd)
                .setHeader("correlationId", correlationId)
                .build();

        boolean sent = this.streamBridge.send("commands-out-0.", msg);

        if (!sent) {
            throw new CommandNotSentException();
        }

        Reply<?> reply;

        try {
            reply = (Reply<?>) future.get(Duration.ofSeconds(5).toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutCommandException | TimeoutException e) {
            throw new TimeoutCommandException("auth-service_CREATE");
        }

        ObjectMapper mapper = new ObjectMapper();
        CreateUserReply keycloakReply = mapper.convertValue(reply.body(), CreateUserReply.class);

        UserEntity user = new UserEntity();
        user.setDocumentId(req.documentId());
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        user.setKeycloackId(keycloakReply.keycloakId());

        return UserMapper.toCreateUserResponse(userRepository.save(user));
    }

}
