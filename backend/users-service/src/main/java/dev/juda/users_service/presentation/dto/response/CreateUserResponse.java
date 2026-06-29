package dev.juda.users_service.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserResponse(
    UUID id,
    String name,
    String documentId,
    UUID keycloakId,
    LocalDateTime createdAt
) {

}
