package dev.juda.users_service.services;

import dev.juda.users_service.models.dto.request.CreateUserRequest;
import dev.juda.users_service.models.dto.response.CreateUserResponse;

public interface UserService {
    CreateUserResponse create(CreateUserRequest req);
}
