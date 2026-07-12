package dev.juda.certificate_service.presentation.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CertificateRequest(
                @NotNull UUID userId,
                @NotNull UUID templateId) {

}
