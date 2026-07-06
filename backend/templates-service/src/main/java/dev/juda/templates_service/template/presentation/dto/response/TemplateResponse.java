package dev.juda.templates_service.template.presentation.dto.response;

import java.util.List;

import dev.juda.templates_service.template.persistence.entity.Template;

public record TemplateResponse(
                String name,
                String departmentName,
                List<String> fields) {

        public static TemplateResponse from(Template t, String departmentName) {
                return new TemplateResponse(
                                t.getName(),
                                departmentName,
                                t.getFields());
        }
}
