package dev.juda.error.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import dev.juda.result.HttpStatus;
import dev.juda.result.Result;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String timestammp,
    int status,
    String error,
    String code,
    String message,
    String path,
    List<FieldError> errors
) {
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FieldError(String field, String message) {}

    public static ErrorResponse of(HttpStatus httpStatus, String message, String path, List<Result.ValidationError> validationErrors) {
        int status = HttpStatus.httpStatus(httpStatus);

        List<FieldError> fieldErrors = null;
        if (validationErrors != null && !validationErrors.isEmpty()) {
            fieldErrors = validationErrors.stream()
                .map(ve -> new FieldError(ve.field(), ve.message()))
                .toList();
        }

        return new ErrorResponse(
            Instant.now().toString(),
            status,
            httpStatus.getErrorPhrase(),
            httpStatus.name(),
            message,
            path,
            fieldErrors
        );
    }

    public static ErrorResponse internalError(String message, String path) {
        return new ErrorResponse(
            Instant.now().toString(),
            HttpStatus.INTERNAL_SERVER_ERROR.getCode(),
            "Internal Server Error",
            HttpStatus.INTERNAL_SERVER_ERROR.name(),
            message,
            path,
            null
        );
    }
}
