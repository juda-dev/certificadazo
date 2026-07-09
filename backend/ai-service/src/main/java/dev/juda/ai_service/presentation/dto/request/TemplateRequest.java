package dev.juda.ai_service.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TemplateRequest(
        @NotBlank String previewSrc,
        @NotNull UUID departmentid) {

}
