package dev.juda.departments_service.department.presentation.dto.response;

import dev.juda.departments_service.department.persistence.entity.Department;

public record DepartmentResponse(
        String name) {

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(department.getName());
    }
}
