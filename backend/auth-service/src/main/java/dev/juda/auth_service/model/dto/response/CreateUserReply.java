package dev.juda.auth_service.model.dto.response;

import java.util.UUID;

public record CreateUserReply(
    UUID keycloakId
) {

}
