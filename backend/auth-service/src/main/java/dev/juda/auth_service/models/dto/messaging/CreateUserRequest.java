package dev.juda.auth_service.models.dto.messaging;

public record CreateUserRequest(
    String name,
    String documentId,
    String password
) {

}
