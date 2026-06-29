package dev.juda.users_service.util.mapper;

import dev.juda.users_service.persistence.entity.UserEntity;
import dev.juda.users_service.presentation.dto.response.CreateUserResponse;

public final class UserMapper {
    private UserMapper(){}

    public static CreateUserResponse toCreateUserResponse(UserEntity user){
        return new CreateUserResponse(user.getId(), user.getName(), user.getDocumentId(), user.getKeycloackId(), user.getCreatedAt());
    }
}
