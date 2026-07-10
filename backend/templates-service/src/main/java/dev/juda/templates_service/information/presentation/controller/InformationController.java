package dev.juda.templates_service.information.presentation.controller;

import java.util.Set;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.templates_service.information.persistence.embeddable.InformationId;
import dev.juda.templates_service.information.presentation.dto.request.InformationRequest;
import dev.juda.templates_service.information.presentation.dto.request.ReadInformationRequest;
import dev.juda.templates_service.information.presentation.dto.response.InformationResponse;
import dev.juda.templates_service.information.presentation.dto.response.ReadInformationResponse;
import dev.juda.templates_service.information.service.interfaces.InformationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/templates/information")
public class InformationController {

    private final InformationService informationService;

    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping
    public ReadInformationResponse read(@Valid @RequestBody ReadInformationRequest req) {
        return informationService.read(req);
    }

    @PutMapping("/{templateId}/{userId}")
    public InformationResponse update(
            @PathVariable(required = true) UUID templateId, @PathVariable(required = true) UUID userId,
            @Valid @RequestBody InformationRequest req) {
        return informationService.update(
                new InformationId(templateId, userId),
                req);
    }

    @DeleteMapping("/{templateId}/{userId}")
    public void delete(@PathVariable(required = true) UUID templateId, @PathVariable(required = true) UUID userId) {
        informationService.delete(new InformationId(templateId, userId));
    }
}
