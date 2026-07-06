package dev.juda.templates_service.template.presentation.dto.response;

import java.util.List;
import java.util.Map;

import dev.juda.templates_service.template.persistence.entity.Template;

public record ReadTemplateResponse(
                String name,
                String desing,
                List<String> fields,
                Map<String, Object> imagesSrc) {

        public static ReadTemplateResponse from(Template t) {
                return new ReadTemplateResponse(
                                t.getName(),
                                t.getDesing(),
                                t.getFields(),
                                t.getImagesSrc());
        }
}
