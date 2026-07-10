package dev.juda.ai_service.shared.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.shared.util.enums.SupportedFileType;

public interface FileValidator {
    String validate(MultipartFile file, SupportedFileType expectedType);
}
