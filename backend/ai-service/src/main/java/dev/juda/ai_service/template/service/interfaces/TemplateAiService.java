package dev.juda.ai_service.template.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.template.presentation.dto.in.TemplateResponse;
import dev.juda.ai_service.template.presentation.dto.request.TemplateRequest;

public interface TemplateAiService {
    TemplateResponse createTemplate(MultipartFile file, TemplateRequest req);
}
