package dev.juda.users_service.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.juda.users_service.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);

    boolean existsByDocumentId(String documentId);

    Optional<UserEntity> findByIdAndEnabledTrue(UUID id);

    @Query("SELECT u.id FROM UserEntity u WHERE u.documentId = :documentId")
    Optional<UUID> findIdByDocumentId(@Param("documentId") String documentId);

    @Query("SELECT u.id FROM UserEntity u WHERE u.email = :email")
    Optional<UUID> findIdByEmail(@Param("email") String email);
}
