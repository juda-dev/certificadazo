package dev.juda.departments_service.position.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.departments_service.position.presentation.dto.request.PositionRequest;
import dev.juda.departments_service.position.presentation.dto.response.PositionResponse;
import dev.juda.departments_service.position.service.interfaces.PositionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/departments/positions")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    public PositionResponse createPosition(@Valid @RequestBody PositionRequest req) {
        return positionService.create(req);
    }

    @DeleteMapping("/{id}")
    public void deletePosition(@PathVariable UUID id) {
        positionService.delete(id);
    }

    @GetMapping
    public Page<PositionResponse> readAll(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return positionService.readAll(pageable);
    }

    @GetMapping("/{id}")
    public PositionResponse read(@PathVariable UUID id) {
        return positionService.read(id);
    }

    @PutMapping("/{id}")
    public PositionResponse update(@PathVariable UUID id, @Valid @RequestBody PositionRequest req) {
        return positionService.update(id, req);
    }

}
