package dev.juda.certificate_service.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.certificate_service.persistence.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {

}
