package dev.juda.error.exception;

import dev.juda.result.HttpStatus;

public class ApplicationException extends BaseException {
    public ApplicationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public ApplicationException(HttpStatus httpStatus, String message, Throwable cause) {
        super(httpStatus, message, cause);
    }

}
