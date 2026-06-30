package dev.juda.auth_service.presentation.dto.response;

import java.util.UUID;

public record CreateUserReply(
    UUID keycloakId
) {

}
