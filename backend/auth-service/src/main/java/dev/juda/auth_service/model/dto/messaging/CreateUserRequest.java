package dev.juda.auth_service.model.dto.messaging;

public record CreateUserRequest(
    String name,
    String documentId,
    String password
) {

}
