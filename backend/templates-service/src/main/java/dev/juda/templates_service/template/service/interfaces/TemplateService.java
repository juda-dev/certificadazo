package dev.juda.templates_service.template.service.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.juda.templates_service.template.presentation.dto.request.TemplateRequest;
import dev.juda.templates_service.template.presentation.dto.response.ReadAllTemplateResponse;
import dev.juda.templates_service.template.presentation.dto.response.ReadTemplateResponse;
import dev.juda.templates_service.template.presentation.dto.response.TemplateResponse;

public interface TemplateService {

    TemplateResponse create(TemplateRequest req);

    ReadTemplateResponse read(UUID id);

    Page<ReadAllTemplateResponse> readAll(Pageable pageable);

    TemplateResponse update(UUID id, TemplateRequest req);

    void delete(UUID id);
}
