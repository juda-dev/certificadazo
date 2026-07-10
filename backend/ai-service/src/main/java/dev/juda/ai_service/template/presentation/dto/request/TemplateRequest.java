package dev.juda.ai_service.template.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record TemplateRequest(
        @NotNull UUID departmentId) {

}
