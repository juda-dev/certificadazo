package dev.juda.auth_service.messaging.dto.in;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String email) {

}
