package dev.juda.departments_service.user_department.util.enums;

public enum UserDepartmentErrorCatalog {
    NON_EXISTS_USER("ERR_USER-DEPARTMENT_001", "No user with these properties was found."),
    USER_DEPARTMENT_NOT_FOUND("ERR_USER-DEPARTMENT_002", "No user department with these properties has been found."),
    USER_DEPARTMENT_ALREADY_EXISTS("ERR_USER-DEPARTMENT_003",
            "The user already has a position in this department; they can update it, but not create a new one.");

    private final String code;
    private final String message;

    private UserDepartmentErrorCatalog(String code, String message) {
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
