package dev.juda.certificate_service.presentation.dto.request;

import java.util.UUID;

public record CertificateRequest(
        UUID userId,
        UUID templateId) {

}
