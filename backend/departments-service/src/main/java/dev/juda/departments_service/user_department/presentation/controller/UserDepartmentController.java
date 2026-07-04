package dev.juda.departments_service.user_department.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.user_department.presentation.dto.request.DeleteUserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.request.UserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.response.UserDepartmentResponse;
import dev.juda.departments_service.user_department.service.interfaces.UserDepartmentService;

@RestController
@RequestMapping("/departments/user-departments")
public class UserDepartmentController {

    private final UserDepartmentService userDepartmentService;

    public UserDepartmentController(UserDepartmentService userDepartmentService) {
        this.userDepartmentService = userDepartmentService;
    }

    @PostMapping
    public UserDepartmentResponse createUserDepartment(@RequestBody UserDepartmentRequest req) {
        return userDepartmentService.create(req);
    }

    @DeleteMapping
    public void deleteUserDepartment(@RequestBody DeleteUserDepartmentRequest req) {
        userDepartmentService.delete(req);
    }

    @GetMapping("/{id}")
    public List<Department> departmentsPerUser(@PathVariable UUID id) {
        return userDepartmentService.departmentsPerUser(id);
    }

    @PutMapping
    public UserDepartmentResponse updateUserDepartment(@RequestBody UserDepartmentRequest req) {
        return userDepartmentService.update(req);
    }

}
