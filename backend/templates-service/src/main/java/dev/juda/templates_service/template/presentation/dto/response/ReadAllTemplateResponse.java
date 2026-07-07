package dev.juda.templates_service.template.presentation.dto.response;

import java.util.Set;
import java.util.UUID;

import dev.juda.templates_service.template.persistence.entity.Template;

public record ReadAllTemplateResponse(
                UUID id,
                String name,
                String departmentName,
                String previewSrc,
                Set<String> fields) {

        public static ReadAllTemplateResponse from(Template t, String departmentName) {
                return new ReadAllTemplateResponse(
                                t.getId(),
                                t.getName(),
                                departmentName,
                                t.getPreviewSrc(),
                                t.getFields());
        }
}
