package dev.juda.templates_service.template.service.interfaces;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.juda.templates_service.shared.messaging.dto.in.TemplateAiResponse;
import dev.juda.templates_service.template.presentation.dto.response.ReadAllTemplateResponse;
import dev.juda.templates_service.template.presentation.dto.response.ReadTemplateResponse;
import dev.juda.templates_service.template.presentation.dto.response.TemplateResponse;

public interface TemplateService {

    TemplateResponse create(TemplateAiResponse req);

    ReadTemplateResponse read(UUID id);

    Page<ReadAllTemplateResponse> readAll(Pageable pageable);

    TemplateResponse update(UUID id, TemplateAiResponse req);

    void delete(UUID id);

    Set<String> readFieldsById(UUID id);

    Boolean existsById(UUID id);
}
