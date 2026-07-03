package dev.juda.departments_service.department.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequest(@NotBlank String name) {

}
