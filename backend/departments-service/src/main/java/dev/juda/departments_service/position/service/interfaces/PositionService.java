package dev.juda.departments_service.position.service.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.juda.departments_service.position.presentation.dto.request.PositionRequest;
import dev.juda.departments_service.position.presentation.dto.response.PositionResponse;

public interface PositionService {
    PositionResponse create(PositionRequest req);

    PositionResponse read(UUID id);

    Page<PositionResponse> readAll(Pageable pageable);

    PositionResponse update(UUID id, PositionRequest req);

    void delete(UUID id);
}
