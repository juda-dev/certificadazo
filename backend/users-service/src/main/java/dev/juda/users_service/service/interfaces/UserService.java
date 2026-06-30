package dev.juda.users_service.service.interfaces;

import java.util.UUID;

import dev.juda.users_service.presentation.dto.request.CreateUserRequest;
import dev.juda.users_service.presentation.dto.request.UpdateUserRequest;
import dev.juda.users_service.presentation.dto.response.UserResponse;

public interface UserService {
    UserResponse create(CreateUserRequest req);
    UserResponse update(UUID id, UpdateUserRequest req);
}
