package dev.juda.departments_service.department.util.enums;

public enum DepartmentErrorCatalog {
    DEPARTMENT_ALREADY_EXISTS("ERR_DEPARTMENT_001", "A department with these properties already exists."),
    DEPARTMENT_NOT_FOUND("ERR_DEPARTMENT_002", "No apartment with these properties has been found.");

    private final String code;
    private final String message;

    private DepartmentErrorCatalog(String code, String message) {
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
