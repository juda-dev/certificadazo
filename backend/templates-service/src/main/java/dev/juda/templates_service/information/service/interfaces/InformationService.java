package dev.juda.templates_service.information.service.interfaces;

import dev.juda.templates_service.information.persistence.embeddable.InformationId;
import dev.juda.templates_service.information.presentation.dto.request.InformationRequest;
import dev.juda.templates_service.information.presentation.dto.request.ReadInformationRequest;
import dev.juda.templates_service.information.presentation.dto.response.InformationResponse;
import dev.juda.templates_service.information.presentation.dto.response.ReadInformationResponse;

public interface InformationService {

    InformationResponse create(InformationRequest req);

    ReadInformationResponse read(ReadInformationRequest req);

    InformationResponse update(InformationId id, InformationRequest req);

    void delete(InformationId id);
}
