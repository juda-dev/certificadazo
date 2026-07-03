package dev.juda.departments_service.position.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PositionRequest(
        @NotBlank String name,
        @NotNull UUID departmentId) {

}
