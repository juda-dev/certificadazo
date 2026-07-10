package dev.juda.ai_service.shared.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface CsvConverter {
    String toPlainText(MultipartFile file);
}
