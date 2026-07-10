package dev.juda.templates_service.template.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.templates_service.shared.messaging.dto.in.TemplateAiResponse;
import dev.juda.templates_service.template.presentation.dto.response.ReadAllTemplateResponse;
import dev.juda.templates_service.template.presentation.dto.response.ReadTemplateResponse;
import dev.juda.templates_service.template.presentation.dto.response.TemplateResponse;
import dev.juda.templates_service.template.service.interfaces.TemplateService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        templateService.delete(id);
    }

    @GetMapping("/{id}")
    public ReadTemplateResponse read(@PathVariable UUID id) {
        return templateService.read(id);
    }

    @GetMapping
    public Page<ReadAllTemplateResponse> readAll(Pageable pageable) {
        return templateService.readAll(pageable);
    }

}
