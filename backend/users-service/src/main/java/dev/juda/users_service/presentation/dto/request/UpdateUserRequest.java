package dev.juda.users_service.presentation.dto.request;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String email) {

}
