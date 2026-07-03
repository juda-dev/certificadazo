package dev.juda.departments_service.department.service.interfaces;

import java.util.UUID;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.presentation.dto.request.DepartmentRequest;

public interface DepartmentService {
    Department create(DepartmentRequest req);

    Department update(UUID id, DepartmentRequest req);

    void delete(UUID id);
}
