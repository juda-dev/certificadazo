package dev.juda.auth_service.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        HttpStatus status,
        String message,
        List<String> detailsMessages,
        LocalDateTime timestamp) {

}
