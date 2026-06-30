package dev.juda.users_service.util.mapper;

import dev.juda.users_service.persistence.entity.UserEntity;
import dev.juda.users_service.presentation.dto.response.UserResponse;

public final class UserMapper {
    private UserMapper() {
    }

    public static UserResponse toUserResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDocumentId(),
                user.getKeycloackId(),
                user.getCreatedAt());
    }
}
