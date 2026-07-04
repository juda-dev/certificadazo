package dev.juda.departments_service.user_department.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.departments_service.user_department.persistence.embeddable.UserDepartmentId;
import dev.juda.departments_service.user_department.persistence.entity.UserDepartment;

public interface UserDepartmentRepository extends JpaRepository<UserDepartment, UserDepartmentId> {

}
