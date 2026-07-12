package dev.juda.certificate_service.service.implementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import dev.juda.certificate_service.persistence.entity.Certificate;
import dev.juda.certificate_service.persistence.repository.CertificateRepository;
import dev.juda.certificate_service.presentation.dto.in.ReadInformationResponse;
import dev.juda.certificate_service.presentation.dto.request.CertificateRequest;
import dev.juda.certificate_service.presentation.dto.response.CertificateResponse;
import dev.juda.certificate_service.service.exception.UserNotFoundException;
import dev.juda.certificate_service.service.interfaces.CertificateService;
import dev.juda.certificate_service.service.interfaces.FileStorageService;
import dev.juda.certificate_service.service.interfaces.HtmlService;
import dev.juda.certificate_service.util.CodeGenerator;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository repository;
    private final RestClient usersRestClient;
    private final RestClient templatesRestClient;
    private final HtmlService htmlService;
    private final FileStorageService fileStorageService;

    public CertificateServiceImpl(CertificateRepository repository,
            @Qualifier("users") RestClient usersRestClient,
            @Qualifier("templates") RestClient templatesRestClient, HtmlService htmlService,
            FileStorageService fileStorageService) {
        this.repository = repository;
        this.usersRestClient = usersRestClient;
        this.templatesRestClient = templatesRestClient;
        this.htmlService = htmlService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public CertificateResponse create(CertificateRequest req) {

        validateExistsUser(req.userId());

        var informationResponse = fetchInformation(req.userId(), req.templateId());

        byte[] pdfCertificate = htmlService.generatePdf(informationResponse.desing(), informationResponse.data());

        String src = fileStorageService.saveCertificate(pdfCertificate);

        String code = CodeGenerator.generateCode();

        return CertificateResponse.from(repository.save(
                new Certificate(code, src, req.userId(), req.templateId())));

    }

    private void validateExistsUser(UUID userId) {
        Boolean existsUser = usersRestClient.get()
                .uri("/exists/{id}", userId)
                .retrieve()
                .body(Boolean.class);

        if (!existsUser)
            throw new UserNotFoundException();
    }

    private ReadInformationResponse fetchInformation(UUID userId, UUID templateId) {
        return templatesRestClient.get()
                .uri("/information/{userId}/{templateId}", userId, templateId)
                .retrieve()
                .body(ReadInformationResponse.class);
    }
}
