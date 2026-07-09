package dev.juda.ai_service.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.presentation.dto.in.TemplateResponse;
import dev.juda.ai_service.presentation.dto.request.TemplateRequest;

public interface AiService {
    TemplateResponse createTemplate(MultipartFile file, TemplateRequest req);
}
