package dev.juda.templates_service.information.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.templates_service.information.persistence.embeddable.InformationId;
import dev.juda.templates_service.information.presentation.dto.response.ReadInformationResponse;
import dev.juda.templates_service.information.service.interfaces.InformationService;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/templates/information")
public class InformationController {

    private final InformationService informationService;

    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping("/{userId}/{templateId}")
    public ReadInformationResponse read(@NotNull @PathVariable UUID userId, @NotNull @PathVariable UUID templateId) {
        return informationService.read(userId, templateId);
    }

    @DeleteMapping("/{templateId}/{userId}")
    public void delete(@PathVariable(required = true) UUID templateId, @PathVariable(required = true) UUID userId) {
        informationService.delete(new InformationId(templateId, userId));
    }
}
