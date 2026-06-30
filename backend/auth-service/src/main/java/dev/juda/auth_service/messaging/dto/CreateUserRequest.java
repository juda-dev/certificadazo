package dev.juda.auth_service.messaging.dto;

public record CreateUserRequest(
    String name,
    String documentId,
    String password
) {

}
