package dev.juda.templates_service.template.util.enums;

public enum TemplateErrorCatalog {
    DEPARTMENT_NOT_FOUND("ERR_DEPARTMENT_001", "No department with these properties has been found."),
    TEMPLATE_NOT_FOUND("ERR_TEMPLATE_001",
            "No template with these properties has been found.");

    private final String code;
    private final String message;

    private TemplateErrorCatalog(String code, String message) {
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
