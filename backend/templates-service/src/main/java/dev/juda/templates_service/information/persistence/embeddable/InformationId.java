package dev.juda.templates_service.information.persistence.embeddable;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record InformationId(
        @Column(name = "template_id") UUID templateId,
        @Column(name = "user_id") UUID userid) implements Serializable {

}
