package dev.juda.result;

public enum HttpStatus {
    NOT_FOUND(404, "Not Found"),
    VALIDATION(400, "Validation Error"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    CONFLICT(409, "Conflict"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String errorPhrase;

    HttpStatus(int code, String errorPhrase) {
        this.code = code;
        this.errorPhrase = errorPhrase;
    }

    public int getCode() {
        return code;
    }

    public String getErrorPhrase() {
        return errorPhrase;
    }

    public static int httpStatus(HttpStatus hs) {
        return switch (hs) {
            case HttpStatus.NOT_FOUND -> HttpStatus.NOT_FOUND.getCode();
            case HttpStatus.VALIDATION -> HttpStatus.VALIDATION.getCode();
            case HttpStatus.UNAUTHORIZED -> HttpStatus.UNAUTHORIZED.getCode();
            case HttpStatus.FORBIDDEN -> HttpStatus.FORBIDDEN.getCode();
            case HttpStatus.CONFLICT -> HttpStatus.CONFLICT.getCode();
            default -> HttpStatus.INTERNAL_SERVER_ERROR.getCode();
        };
    }
}
