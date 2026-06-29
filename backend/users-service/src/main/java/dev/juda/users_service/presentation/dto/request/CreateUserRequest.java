package dev.juda.users_service.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    @NotBlank String name,
    @NotBlank String documentId,
    @NotBlank String password
) {

}
