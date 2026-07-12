package dev.juda.certificate_service.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.certificate_service.presentation.dto.request.CertificateRequest;
import dev.juda.certificate_service.presentation.dto.response.CertificateResponse;
import dev.juda.certificate_service.service.interfaces.CertificateService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService service;

    public CertificateController(CertificateService service) {
        this.service = service;
    }

    @PostMapping
    public CertificateResponse create(@Valid @RequestBody CertificateRequest req) {
        return service.create(req);
    }
}
