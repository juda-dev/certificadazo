package dev.juda.templates_service.template.service.implementation;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClient;

import dev.juda.templates_service.shared.messaging.dto.in.TemplateAiResponse;
import dev.juda.templates_service.template.persistence.entity.Template;
import dev.juda.templates_service.template.persistence.repository.TemplateRepository;
import dev.juda.templates_service.template.presentation.dto.in.DepartmentResponse;
import dev.juda.templates_service.template.presentation.dto.response.ReadAllTemplateResponse;
import dev.juda.templates_service.template.presentation.dto.response.ReadTemplateResponse;
import dev.juda.templates_service.template.presentation.dto.response.TemplateResponse;
import dev.juda.templates_service.template.presentation.exception.TemplateNotFoundException;
import dev.juda.templates_service.template.service.interfaces.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final RestClient restClient;

    public TemplateServiceImpl(TemplateRepository templateRepository,
            @Qualifier("departmentsRestClient") RestClient restClient) {
        this.templateRepository = templateRepository;
        this.restClient = restClient;
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.CREATED)
    public TemplateResponse create(TemplateAiResponse req) {

        String departmentName = fetchDepartmentName(req.departmentId()).name();

        return persistTemplate(null, req, departmentName);
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(UUID id) {
        Template template = templateRepository.findById(id).orElseThrow(TemplateNotFoundException::new);

        templateRepository.delete(template);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadTemplateResponse read(UUID id) {
        return ReadTemplateResponse.from(
                templateRepository.findById(id)
                        .orElseThrow(TemplateNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadAllTemplateResponse> readAll(Pageable pageable) {
        return templateRepository
                .findAll(pageable).map(t -> {
                    String departmentName = fetchDepartmentName(t.getDepartmentId()).name();

                    return new ReadAllTemplateResponse(
                            t.getId(),
                            t.getName(),
                            departmentName,
                            t.getPreviewSrc(),
                            t.getFields());
                });
    }

    @Override
    @Transactional
    public TemplateResponse update(UUID id, TemplateAiResponse req) {
        String departmentName = fetchDepartmentName(req.departmentId()).name();

        return persistTemplate(id, req, departmentName);
    }

    private DepartmentResponse fetchDepartmentName(UUID departmentId) {
        return restClient.get()
                .uri("{id}", departmentId)
                .retrieve()
                .body(DepartmentResponse.class);
    }

    private TemplateResponse persistTemplate(UUID templateId, TemplateAiResponse req, String departmentName) {
        Template template = (templateId != null) ? templateRepository.findById(templateId)
                .orElseThrow(TemplateNotFoundException::new) : new Template();

        template.setName(req.name());
        template.setDesing(req.desing());
        template.setDepartmentId(req.departmentId());
        template.setPreviewSrc(req.previewSrc());
        template.setFields(req.fields());
        template.setImagesSrc(req.imagesSrc());

        Template saved = templateRepository.save(template);

        return TemplateResponse.from(saved, departmentName);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> readFieldsById(UUID id) {
        return templateRepository.findFieldsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsById(UUID id) {
        return templateRepository.existsById(id);
    }

}
