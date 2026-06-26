package dev.juda.error.exception;

import java.util.List;

import dev.juda.error.dto.ErrorResponse;
import dev.juda.result.HttpStatus;
import dev.juda.result.Result;

public class ValidationError extends BaseException {

    private final transient List<Result.ValidationError> validationErrors;  

    public ValidationError(String message) {
        this.validationErrors = null;
        super(HttpStatus.VALIDATION, message);
    }

    public ValidationError(String message, List<Result.ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
        super(HttpStatus.VALIDATION, message);
    }

    public List<Result.ValidationError> getValidationErrors() {
        return validationErrors;
    }

    @Override
    public ErrorResponse toErrorResponse(String path) {
        return ErrorResponse.of(
            HttpStatus.VALIDATION,
            getMessage(),
            path,
            validationErrors
        );
    }
    
}
