package dev.juda.ai_service.information.presentation.dto.in;

import java.util.Map;

public record InformationResponse(
        String userFullName,
        String templateName,
        Map<String, Object> data) {

}
