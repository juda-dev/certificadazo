package dev.juda.departments_service.user_department.persistence.entity;

import dev.juda.departments_service.user_department.persistence.embeddable.UserDepartmentId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_departments", uniqueConstraints = @UniqueConstraint(name = "uk_user_department", columnNames = {
        "user_id", "department_id" }))
public class UserDepartment {

    @EmbeddedId
    private UserDepartmentId id;

    public UserDepartmentId getId() {
        return id;
    }

    public void setId(UserDepartmentId id) {
        this.id = id;
    }

}
