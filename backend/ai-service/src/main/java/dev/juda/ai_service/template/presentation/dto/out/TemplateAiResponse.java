package dev.juda.ai_service.template.presentation.dto.out;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record TemplateAiResponse(
        String name,
        String desing,
        UUID departmentId,
        String previewSrc,
        Set<String> fields,
        Map<String, Object> imagesSrc) {

}
