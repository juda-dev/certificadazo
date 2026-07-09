package dev.juda.ai_service.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidator {
    String validate(MultipartFile file);
}
