package dev.juda.departments_service.user_department.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
