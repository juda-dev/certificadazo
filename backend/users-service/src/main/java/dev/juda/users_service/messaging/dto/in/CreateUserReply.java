package dev.juda.users_service.messaging.dto.in;

import java.util.UUID;

public record CreateUserReply(
    UUID keycloakId
) {

}
