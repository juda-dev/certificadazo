package dev.juda.certificate_service.service.interfaces;

import dev.juda.certificate_service.presentation.dto.request.CertificateRequest;
import dev.juda.certificate_service.presentation.dto.response.CertificateResponse;

public interface CertificateService {
    CertificateResponse create(CertificateRequest req);
}
