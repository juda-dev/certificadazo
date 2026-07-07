package dev.juda.templates_service.template.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.templates_service.information.presentation.dto.out.NameAndFieldsTemplate;
import dev.juda.templates_service.template.persistence.entity.Template;

public interface TemplateRepository extends JpaRepository<Template, UUID> {

    Optional<NameAndFieldsTemplate> findNameAndFieldsById(UUID id);
}
