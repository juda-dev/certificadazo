package dev.juda.templates_service.template.presentation.dto.request;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TemplateRequest(
        String name,
        String desing,
        UUID departmentId,
        String previewSrc,
        List<String> fields,
        Map<String, Object> imagesSrc) {

}
