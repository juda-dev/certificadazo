package dev.juda.departments_service.position.util.enums;

public enum PositionErrorCatalog {
    POSITION_ALREADY_EXISTS("ERR_POSITION_001", "A position with these properties already exists."),
    POSITION_NOT_FOUND("ERR_POSITION_002", "No position with these properties has been found.");

    private final String code;
    private final String message;

    private PositionErrorCatalog(String code, String message) {
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
