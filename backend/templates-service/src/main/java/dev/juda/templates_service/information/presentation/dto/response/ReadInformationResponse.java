package dev.juda.templates_service.information.presentation.dto.response;

import java.util.Map;

import dev.juda.templates_service.information.persistence.entity.Information;

public record ReadInformationResponse(
                Map<String, Object> data) {

        public static ReadInformationResponse from(Information information) {
                return new ReadInformationResponse(information.getData());
        }
}
