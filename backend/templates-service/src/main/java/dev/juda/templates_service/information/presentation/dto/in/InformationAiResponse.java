package dev.juda.templates_service.information.presentation.dto.in;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InformationAiResponse(
        @NotNull UUID userId,
        @NotNull UUID templateId,
        @NotEmpty Map<String, @Valid Object> data) {


}
