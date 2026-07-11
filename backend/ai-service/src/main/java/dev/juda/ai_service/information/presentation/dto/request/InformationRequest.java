package dev.juda.ai_service.information.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record InformationRequest(
        @NotNull UUID templateId) {

}
