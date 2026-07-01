package dev.juda.auth_service.messaging.dto.in;

public record PasswordChangeRequest(
        String currentPassword,
        String newPassword) {

}
