package dev.juda.departments_service.user_department.persistence.embeddable;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserDepartmentId(

        @Column(name = "user_id") UUID userId,

        @Column(name = "department_id") UUID departmentId,

        @Column(name = "position_id") UUID positionId) implements Serializable {

}
