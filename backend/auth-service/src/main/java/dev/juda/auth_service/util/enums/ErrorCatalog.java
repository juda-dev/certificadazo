package dev.juda.auth_service.util.enums;

public enum ErrorCatalog {
    GENERIC_ERROR("ERR_GENERIC_001", "An unexpected error"),
    BAD_CREDENTIALS("ERR_AUTH_001", "Bad credentials"),
    INVALID_CREDENTIALS("ERR_AUTH_002", "Invalid credentials or unauthorized client"),
    ROLE_NOT_FOUND("ERR_AUTH_003", "Role not found in Keycloak"),
    USER_NOT_CREATED("ERR_AUTH_004", "User not created in Keycloak");

    private final String code;
    private final String message;

    ErrorCatalog(String code, String message){
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
