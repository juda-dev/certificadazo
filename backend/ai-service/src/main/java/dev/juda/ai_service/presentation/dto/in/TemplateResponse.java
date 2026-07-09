package dev.juda.ai_service.presentation.dto.in;

import java.util.Set;

public record TemplateResponse(
        String name,
        String departmentName,
        Set<String> fields) {

}
