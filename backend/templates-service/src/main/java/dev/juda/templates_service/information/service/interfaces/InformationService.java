package dev.juda.templates_service.information.service.interfaces;

import java.util.Set;

import dev.juda.templates_service.information.persistence.embeddable.InformationId;
import dev.juda.templates_service.information.presentation.dto.in.InformationAiResponse;
import dev.juda.templates_service.information.presentation.dto.request.ReadInformationRequest;
import dev.juda.templates_service.information.presentation.dto.response.InformationResponse;
import dev.juda.templates_service.information.presentation.dto.response.ReadInformationResponse;

public interface InformationService {

    Set<InformationResponse> create(Set<InformationAiResponse> req);

    ReadInformationResponse read(ReadInformationRequest req);

    InformationResponse update(InformationId id, InformationAiResponse req);

    void delete(InformationId id);
}
