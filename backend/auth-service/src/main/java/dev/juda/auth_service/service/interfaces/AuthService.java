package dev.juda.auth_service.service.interfaces;

import dev.juda.auth_service.messaging.dto.CreateUserRequest;
import dev.juda.auth_service.models.dto.request.AuthRequest;
import dev.juda.auth_service.models.dto.response.AuthResponse;
import dev.juda.auth_service.models.dto.response.CreateUserReply;

public interface AuthService {
    CreateUserReply create(CreateUserRequest req);
    AuthResponse login(AuthRequest req);
}
