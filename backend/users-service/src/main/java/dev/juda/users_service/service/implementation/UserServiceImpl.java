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
import dev.juda.users_service.presentation.dto.request.PasswordChangeRequest;
import dev.juda.users_service.presentation.dto.request.UpdateUserRequest;
import dev.juda.users_service.presentation.dto.response.UserResponse;
import dev.juda.users_service.service.exception.CommandNotSentException;
import dev.juda.users_service.service.exception.ExistingUserException;
import dev.juda.users_service.service.exception.NonExistentUser;
import dev.juda.users_service.service.exception.TimeoutCommandException;
import dev.juda.users_service.service.interfaces.UserService;
import dev.juda.users_service.util.enums.CommandType;
import dev.juda.users_service.util.mapper.UserMapper;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StreamBridge streamBridge;
    private final ReplyInbox replyInbox;
    private final ObjectMapper mapper;

    public UserServiceImpl(UserRepository userRepository, StreamBridge streamBridge, ReplyInbox replyInbox,
            ObjectMapper mapper) {
        this.userRepository = userRepository;
        this.streamBridge = streamBridge;
        this.replyInbox = replyInbox;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public UserResponse create(CreateUserRequest req) {

        if (userRepository.existsByEmail(req.email())) {
            throw new ExistingUserException("email address.");
        }

        if (userRepository.existsByDocumentId(req.documentId())) {
            throw new ExistingUserException("document id.");
        }

        var cmd = new Command<>(CommandType.CREATE, null, req);

        Reply<?> reply = getReply(cmd, "CREATE");

        ObjectMapper mapper = new ObjectMapper();
        CreateUserReply keycloakReply = mapper.convertValue(reply.body(), CreateUserReply.class);

        UserEntity user = new UserEntity();
        user.setDocumentId(req.documentId());
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        user.setKeycloackId(keycloakReply.keycloakId());

        return UserMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest req) {
        UserEntity user = userRepository.findById(id).orElseThrow(NonExistentUser::new);

        var cmd = new Command<>(CommandType.UPDATE, user.getKeycloackId(), req);

        getReply(cmd, "UPDATE");

        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());

        return UserMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public Reply<String> updatePassword(UUID id, PasswordChangeRequest req) {
        if (!userRepository.existsById(id)) {
            throw new NonExistentUser();
        }

        var cmd = new Command<>(CommandType.PASSWORD_UPDATE, id, req);

        Reply<String> reply = mapper.convertValue(getReply(cmd, "PASSWORD_UPDATE"), new TypeReference<Reply<String>>() {
        });

        return reply;
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
            throw new TimeoutCommandException("auth-service_" + methodName);
        }

        return reply;
    }

}
