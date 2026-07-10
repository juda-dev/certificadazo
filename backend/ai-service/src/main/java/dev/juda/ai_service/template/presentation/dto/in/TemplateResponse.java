package dev.juda.ai_service.template.presentation.dto.in;

import java.util.Set;

public record TemplateResponse(
        String name,
        String departmentName,
        Set<String> fields) {

}
