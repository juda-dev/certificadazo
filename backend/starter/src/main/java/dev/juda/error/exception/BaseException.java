package dev.juda.error.exception;

import dev.juda.error.dto.ErrorResponse;
import dev.juda.result.HttpStatus;

public abstract class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected BaseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    protected BaseException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return httpStatus.name();
    }

    public int getHttpStatus() {
        return httpStatus.getCode();
    }

    public ErrorResponse toErrorResponse(String path) {
        return ErrorResponse.of(
            httpStatus,
            getMessage(),
            path,
            null
        );
    }
}
