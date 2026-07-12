package dev.juda.templates_service.information.presentation.dto.response;

import java.util.Map;

import dev.juda.templates_service.information.persistence.entity.Information;
import dev.juda.templates_service.template.persistence.entity.Template;

public record ReadInformationResponse(
                Map<String, Object> data,
                String desing) {

        public static ReadInformationResponse from(Information information, Template template) {
                Map<String, Object> response = information.getData();
                response.putAll(template.getImagesSrc());
                return new ReadInformationResponse(response, template.getDesing());
        }
}
