package dev.juda.templates_service.template.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.juda.templates_service.template.persistence.entity.Template;

public interface TemplateRepository extends JpaRepository<Template, UUID> {

}
