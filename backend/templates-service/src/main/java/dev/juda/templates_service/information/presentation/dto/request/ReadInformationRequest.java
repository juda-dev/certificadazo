package dev.juda.templates_service.information.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ReadInformationRequest(
        @NotNull UUID userId,
        @NotNull UUID templateId) {

}
