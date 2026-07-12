package dev.juda.users_service.service.interfaces;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import dev.juda.users_service.messaging.dto.in.Reply;
import dev.juda.users_service.presentation.dto.request.CreateUserRequest;
import dev.juda.users_service.presentation.dto.request.PasswordChangeRequest;
import dev.juda.users_service.presentation.dto.request.UpdateUserRequest;
import dev.juda.users_service.presentation.dto.response.UserFullNameView;
import dev.juda.users_service.presentation.dto.response.UserResponse;

public interface UserService {
    UserResponse create(CreateUserRequest req);

    UserResponse update(UUID id, UpdateUserRequest req);

    ResponseEntity<Reply<?>> updatePassword(UUID id, PasswordChangeRequest req);

    void delete(UUID id);

    UserFullNameView userFullNameView(UUID id);

    UUID findIdByDocumentId(String documentid);

    UUID findIdByEmail(String email);

    Boolean existsById(UUID id);
}
