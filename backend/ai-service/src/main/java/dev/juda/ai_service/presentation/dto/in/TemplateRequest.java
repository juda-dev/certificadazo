package dev.juda.ai_service.presentation.dto.in;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record TemplateRequest(
        String name,
        String desing,
        UUID departmentId,
        String previewSrc,
        Set<String> fields,
        Map<String, Object> imagesSrc) {

}
