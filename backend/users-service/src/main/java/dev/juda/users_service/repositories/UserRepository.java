package dev.juda.users_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.users_service.models.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID>{

}
