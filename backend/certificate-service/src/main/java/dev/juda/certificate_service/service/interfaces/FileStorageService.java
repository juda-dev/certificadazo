package dev.juda.certificate_service.service.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface FileStorageService {
    String saveCertificate(byte[] pdfCertificate);

    ResponseEntity<Resource> getCertificate(String filename);
}
