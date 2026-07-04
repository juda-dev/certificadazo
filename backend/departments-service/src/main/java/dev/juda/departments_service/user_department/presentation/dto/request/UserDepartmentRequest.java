package dev.juda.departments_service.user_department.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record UserDepartmentRequest(
        @NotNull UUID userId,
        @NotNull UUID departmentId,
        @NotNull UUID positionId) {

}
