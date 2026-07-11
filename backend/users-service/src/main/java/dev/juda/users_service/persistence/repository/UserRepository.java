package dev.juda.users_service.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.users_service.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);

    Optional<UserEntity> findByIdAndEnabledTrue(UUID id);

    Optional<UUID> findIdByDocumentId(String documentId);

    Optional<UUID> findIdByEmail(String email);
}
