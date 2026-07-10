package dev.juda.ai_service.template.presentation.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.template.presentation.dto.in.TemplateResponse;
import dev.juda.ai_service.template.presentation.dto.request.TemplateRequest;
import dev.juda.ai_service.template.service.interfaces.AiService;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping(value = "/create-template", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TemplateResponse createTemplate(@RequestPart("file") MultipartFile file,
            @RequestPart("data") TemplateRequest req) {
        return aiService.createTemplate(file, req);
    }

}
