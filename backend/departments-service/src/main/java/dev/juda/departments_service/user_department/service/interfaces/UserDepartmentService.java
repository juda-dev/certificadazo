package dev.juda.departments_service.user_department.service.interfaces;

import java.util.List;
import java.util.UUID;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.user_department.presentation.dto.request.DeleteUserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.request.UserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.response.UserDepartmentResponse;

public interface UserDepartmentService {
    UserDepartmentResponse create(UserDepartmentRequest req);

    void delete(DeleteUserDepartmentRequest req);

    UserDepartmentResponse update(UserDepartmentRequest req);

    List<Department> departmentsPerUser(UUID userId);
}
