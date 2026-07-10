package dev.juda.ai_service.information.service.interfaces;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.information.presentation.dto.in.InformationResponse;
import dev.juda.ai_service.information.presentation.dto.request.InformationRequest;

public interface InformationAiService {
    Set<InformationResponse> create(MultipartFile file, InformationRequest req);
}
