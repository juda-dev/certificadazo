package dev.juda.templates_service.information.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.templates_service.information.persistence.embeddable.InformationId;
import dev.juda.templates_service.information.persistence.entity.Information;

public interface InformationRepository extends JpaRepository<Information, InformationId> {

}
