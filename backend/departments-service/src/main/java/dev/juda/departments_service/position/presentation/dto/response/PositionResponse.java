package dev.juda.departments_service.position.presentation.dto.response;

import java.util.UUID;

import dev.juda.departments_service.position.persistence.entity.Position;

public record PositionResponse(
                UUID positionId,
                String name,
                String departmentName) {

        public static PositionResponse from(Position position) {
                return new PositionResponse(
                                position.getId(),
                                position.getName(),
                                position.getDepartment().getName());
        }
}
