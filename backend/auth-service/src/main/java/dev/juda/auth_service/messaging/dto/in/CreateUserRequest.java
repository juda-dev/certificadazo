package dev.juda.auth_service.messaging.dto.in;

public record CreateUserRequest(
        String firstName,
        String lastName,
        String email,
        String documentId,
        String password) {

}
