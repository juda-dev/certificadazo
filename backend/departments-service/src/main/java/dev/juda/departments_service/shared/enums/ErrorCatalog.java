package dev.juda.departments_service.shared.enums;

public enum ErrorCatalog {
    GENERIC_ERROR("ERR_GENERIC_001", "An unexpected error"),
    BAD_CREDENTIALS("ERR_BAD_CREDENTIALS_001", "Bad credentials");

    private final String code;
    private final String message;

    ErrorCatalog(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}