package dev.juda.users_service.service.implementation;

import dev.juda.users_service.messaging.app.ReplyInbox;
import dev.juda.users_service.messaging.dto.in.CreateUserReply;
import dev.juda.users_service.messaging.dto.in.Reply;
import dev.juda.users_service.messaging.dto.out.Command;
import dev.juda.users_service.persistence.entity.UserEntity;
import dev.juda.users_service.persistence.repository.UserRepository;
import dev.juda.users_service.presentation.dto.request.CreateUserRequest;
import dev.juda.users_service.presentation.dto.request.PasswordChangeRequest;
import dev.juda.users_service.presentation.dto.request.UpdateUserRequest;
import dev.juda.users_service.presentation.dto.response.UserFullNameView;
import dev.juda.users_service.presentation.dto.response.UserResponse;
import dev.juda.users_service.service.exception.CommandNotSentException;
import dev.juda.users_service.service.exception.ExistingUserException;
import dev.juda.users_service.service.exception.NonExistentUser;
import dev.juda.users_service.service.exception.TimeoutCommandException;
import dev.juda.users_service.service.interfaces.UserService;
import dev.juda.users_service.util.enums.CommandType;
import dev.juda.users_service.util.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StreamBridge streamBridge;
    private final ReplyInbox replyInbox;
    private final ObjectMapper mapper;
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

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
        LOG.trace("Starting user creation with document {} and email {}", req.documentId(), req.email());

        if (userRepository.existsByEmail(req.email())) {
            LOG.warn("A user with the email address {} already exists", req.email());
            throw new ExistingUserException("email address.");
        }

        if (userRepository.existsByDocumentId(req.documentId())) {
            LOG.warn("A user already exists with the document {}", req.documentId());
            throw new ExistingUserException("document id.");
        }

        var cmd = new Command<>(CommandType.CREATE, null, req);

        Reply<?> reply = getReply(cmd, "CREATE");
        LOG.debug("The response is received {}", reply.body());

        LOG.trace("Converting response into DTO");
        CreateUserReply keycloakReply = mapper.convertValue(reply.body(), CreateUserReply.class);

        UserEntity user = new UserEntity();
        user.setDocumentId(req.documentId());
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        user.setKeycloackId(keycloakReply.keycloakId());

        LOG.info("User creation completes");
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest req) {
        LOG.trace("Starting user update {}", id);

        UserEntity user = userRepository.findById(id).orElseThrow(NonExistentUser::new);

        var cmd = new Command<>(CommandType.UPDATE, user.getKeycloackId(), req);

        getReply(cmd, "UPDATE");

        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());

        LOG.info("The user update process {} ends", id);
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public ResponseEntity<Reply<?>> updatePassword(UUID id, PasswordChangeRequest req) {
        LOG.trace("Start password update for user {}", id);

        UserEntity user = userRepository.findById(id).orElseThrow(NonExistentUser::new);

        var cmd = new Command<>(CommandType.PASSWORD_UPDATE, user.getKeycloackId(), req);

        Reply<?> reply = mapper.convertValue(getReply(cmd, "PASSWORD_UPDATE"), new TypeReference<Reply<?>>() {
        });
        LOG.debug("The response is received {}", reply.body());

        return switch (reply.status()) {
            case SUCCESS -> ResponseEntity.ok(reply);
            case ERROR -> ResponseEntity.badRequest().body(reply);
        };
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        LOG.trace("Start user deactivation {}", id);
        UserEntity userEntity = userRepository.findByIdAndEnabledTrue(id).orElseThrow(NonExistentUser::new);

        var cmd = new Command<>(CommandType.DELETE, userEntity.getKeycloackId(), null);

        getReply(cmd, "DELETE");

        userEntity.setEnabled(false);
        userRepository.save(userEntity);

        LOG.info("User {} successfully deactivated", id);
    }

    private Reply<?> getReply(Command<?> cmd, String methodName) {
        LOG.trace("Initiating authentication service response");

        String correlationId = UUID.randomUUID().toString();
        var future = replyInbox.register(correlationId);

        var msg = MessageBuilder
                .withPayload(cmd)
                .setHeader("correlationId", correlationId)
                .build();

        boolean sent = this.streamBridge.send("commands-out-0", msg);

        if (!sent) {
            LOG.error("Error sending command {}", cmd);
            throw new CommandNotSentException();
        }

        Reply<?> reply;

        try {
            reply = (Reply<?>) future.get(Duration.ofSeconds(5).toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutCommandException | TimeoutException e) {
            LOG.error("Error or timeout while trying to receive a response from the authentication service");
            throw new TimeoutCommandException("auth-service_" + methodName);
        }

        LOG.info("Response received successfully");
        return reply;
    }

    @Override
    @Transactional(readOnly = true)
    public UserFullNameView userFullNameView(UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(NonExistentUser::new);

        return new UserFullNameView(user.getFirstName() + " " + user.getLastName());
    }

    @Override
    @Transactional(readOnly = true)
    public UUID findIdByDocumentId(String documentId) {
        LOG.trace("Starting to obtain the user's full name {}", documentId);
        return userRepository.findIdByDocumentId(documentId).orElseThrow(NonExistentUser::new);
    }

    @Override
    @Transactional(readOnly = true)
    public UUID findIdByEmail(String email) {
        LOG.trace("Starting to obtain the user ID with email {}", email);
        return userRepository.findIdByEmail(email).orElseThrow(NonExistentUser::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

}
