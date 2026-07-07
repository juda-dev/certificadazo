package dev.juda.templates_service.information.service.interfaces;

import java.util.UUID;

import dev.juda.templates_service.information.presentation.dto.request.InformationRequest;
import dev.juda.templates_service.information.presentation.dto.request.ReadInformationRequest;
import dev.juda.templates_service.information.presentation.dto.response.InformationResponse;
import dev.juda.templates_service.information.presentation.dto.response.ReadInformationResponse;

public interface InformationService {

    InformationResponse create(InformationRequest req);

    ReadInformationResponse read(ReadInformationRequest req);

    InformationResponse update(InformationRequest req);

    void delete(UUID id);
}
