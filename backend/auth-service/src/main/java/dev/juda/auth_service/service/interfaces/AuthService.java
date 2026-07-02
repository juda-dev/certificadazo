package dev.juda.auth_service.service.interfaces;

import java.util.UUID;

import dev.juda.auth_service.messaging.dto.in.CreateUserRequest;
import dev.juda.auth_service.messaging.dto.in.PasswordChangeRequest;
import dev.juda.auth_service.messaging.dto.in.UpdateUserRequest;
import dev.juda.auth_service.messaging.dto.out.Reply;
import dev.juda.auth_service.presentation.dto.request.AuthRequest;
import dev.juda.auth_service.presentation.dto.response.AuthResponse;
import dev.juda.auth_service.presentation.dto.response.CreateUserReply;

public interface AuthService {
    CreateUserReply create(CreateUserRequest req);
    AuthResponse login(AuthRequest req);
    void update(UUID userId ,UpdateUserRequest req);
    Reply<?> updatePassword(UUID userId, PasswordChangeRequest req);
    void delete(UUID userId);
}
