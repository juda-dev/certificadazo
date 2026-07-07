package dev.juda.templates_service.information.presentation.dto.response;

import java.util.Map;

public record InformationResponse(
        String userFullName,
        String templateName,
        Map<String, Object> data) {

}
