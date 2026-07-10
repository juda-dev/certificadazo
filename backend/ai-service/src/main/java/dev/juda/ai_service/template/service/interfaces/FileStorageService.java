package dev.juda.ai_service.template.service.interfaces;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String savePreview(MultipartFile file);

    ResponseEntity<Resource> getImagePreview(String filename);
}
