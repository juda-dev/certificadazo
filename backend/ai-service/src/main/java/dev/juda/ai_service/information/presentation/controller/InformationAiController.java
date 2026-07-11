package dev.juda.ai_service.information.presentation.controller;

import java.util.Set;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.juda.ai_service.information.presentation.dto.in.InformationResponse;
import dev.juda.ai_service.information.presentation.dto.request.InformationRequest;
import dev.juda.ai_service.information.service.interfaces.InformationAiService;
import jakarta.validation.Valid;

@RestController
public class InformationAiController {

    private final InformationAiService service;

    public InformationAiController(InformationAiService service) {
        this.service = service;
    }

    @PostMapping(value = "/create-information", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Set<InformationResponse> createInformation(@RequestPart("file") MultipartFile file,
            @Valid @RequestPart("data") InformationRequest req) {
        return service.create(file, req);
    }
}
