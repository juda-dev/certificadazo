package dev.juda.users_service.service.interfaces;

import dev.juda.users_service.presentation.dto.request.CreateUserRequest;
import dev.juda.users_service.presentation.dto.response.CreateUserResponse;

public interface UserService {
    CreateUserResponse create(CreateUserRequest req);
}
