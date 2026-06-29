package dev.juda.users_service.mappers;

import dev.juda.users_service.models.dto.response.CreateUserResponse;
import dev.juda.users_service.models.entities.UserEntity;

public final class UserMapper {
    private UserMapper(){}

    public static CreateUserResponse toCreateUserResponse(UserEntity user){
        return new CreateUserResponse(user.getId(), user.getName(), user.getDocumentId(), user.getKeycloackId(), user.getCreatedAt());
    }
}
