package dev.juda.ai_service.information.presentation.dto.response;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InformationAiWithoutIdResponse(
        @NotBlank String documentId,
        @NotBlank String email,
        @NotNull UUID templateId,
        @NotEmpty Map<String, @Valid Object> data) {

}
