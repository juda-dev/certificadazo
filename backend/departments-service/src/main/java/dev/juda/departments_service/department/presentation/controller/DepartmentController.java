package dev.juda.departments_service.department.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.presentation.dto.request.DepartmentRequest;
import dev.juda.departments_service.department.service.interfaces.DepartmentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public Department createDepartment(@Valid @RequestBody DepartmentRequest req) {
        return departmentService.create(req);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable UUID id, @RequestBody DepartmentRequest req) {
        return departmentService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable UUID id) {
        departmentService.delete(id);
    }

}
