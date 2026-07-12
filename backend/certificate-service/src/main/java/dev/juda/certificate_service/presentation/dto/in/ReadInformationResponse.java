package dev.juda.certificate_service.presentation.dto.in;

import java.util.Map;

public record ReadInformationResponse(
                Map<String, Object> data,
                String desing) {

}
