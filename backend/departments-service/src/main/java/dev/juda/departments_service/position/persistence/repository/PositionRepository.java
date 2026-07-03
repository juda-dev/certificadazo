package dev.juda.departments_service.position.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.departments_service.position.persistence.entity.Position;

public interface PositionRepository extends JpaRepository<Position, UUID> {
    boolean existsByName(String name);
}
