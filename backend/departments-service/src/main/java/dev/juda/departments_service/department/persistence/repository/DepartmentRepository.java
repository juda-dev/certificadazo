package dev.juda.departments_service.department.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.departments_service.department.persistence.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String name);

}
