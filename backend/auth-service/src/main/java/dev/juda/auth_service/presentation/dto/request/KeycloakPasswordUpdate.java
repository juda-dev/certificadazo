package dev.juda.auth_service.presentation.dto.request;

public record KeycloakPasswordUpdate(
        String currentPassword,
        String newPassword,
        String confirmation) {

}
