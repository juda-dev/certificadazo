package dev.juda.auth_service.services;

import dev.juda.auth_service.model.dto.messaging.CreateUserRequest;
import dev.juda.auth_service.model.dto.response.CreateUserReply;

public interface AuthService {
    CreateUserReply create(CreateUserRequest req);
}
