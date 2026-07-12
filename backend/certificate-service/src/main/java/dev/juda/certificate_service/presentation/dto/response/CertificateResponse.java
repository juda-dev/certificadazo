package dev.juda.certificate_service.presentation.dto.response;

import dev.juda.certificate_service.persistence.entity.Certificate;

public record CertificateResponse(
                String code,
                String src) {

        public static CertificateResponse from(Certificate certificate) {
                return new CertificateResponse(certificate.getCode(), certificate.getSrc());
        }

}
