package dev.juda.departments_service.user_department.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.departments_service.user_department.persistence.embeddable.UserDepartmentId;
import dev.juda.departments_service.user_department.persistence.entity.UserDepartment;

public interface UserDepartmentRepository extends JpaRepository<UserDepartment, UserDepartmentId> {
    Optional<UserDepartment> findById_UserIdAndId_DepartmentId(UUID userId, UUID departmentId);

    List<UserDepartment> findById_UserId(UUID userId);

    boolean existsById_UserIdAndId_DepartmentId(UUID userId, UUID departmentId);
}
