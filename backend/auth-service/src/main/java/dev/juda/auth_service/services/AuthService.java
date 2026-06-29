package dev.juda.auth_service.services;

import dev.juda.auth_service.models.dto.messaging.CreateUserRequest;
import dev.juda.auth_service.models.dto.response.CreateUserReply;

public interface AuthService {
    CreateUserReply create(CreateUserRequest req);
}
