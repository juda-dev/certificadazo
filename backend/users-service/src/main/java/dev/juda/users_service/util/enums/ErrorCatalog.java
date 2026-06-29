package dev.juda.users_service.util.enums;

public enum ErrorCatalog {
    GENERIC_ERROR("ERR_GENERIC_001", "An unexpected error"),
    BAD_CREDENTIALS("ERR_BAD_CREDENTIALS_001", "Bad credentials"),
    COMMAND_NOT_SENT("ERR_USER_001","The Kafka command couldn't be sent"),
    TIMEOUT_COMMAND("ERR_USER_002", "Timeout waiting for response from the sent command");    

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
