package dev.juda.auth_service.models.dto.response;

import java.util.UUID;

public record CreateUserReply(
    UUID keycloakId
) {

}
