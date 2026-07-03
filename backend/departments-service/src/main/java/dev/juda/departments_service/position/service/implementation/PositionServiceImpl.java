package dev.juda.departments_service.position.service.implementation;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.persistence.repository.DepartmentRepository;
import dev.juda.departments_service.department.presentation.exception.DepartmentNotFound;
import dev.juda.departments_service.position.persistence.entity.Position;
import dev.juda.departments_service.position.persistence.repository.PositionRepository;
import dev.juda.departments_service.position.presentation.dto.request.PositionRequest;
import dev.juda.departments_service.position.presentation.dto.response.PositionResponse;
import dev.juda.departments_service.position.presentation.exception.PositionAlreadyExistsException;
import dev.juda.departments_service.position.presentation.exception.PositionNotFoundException;
import dev.juda.departments_service.position.service.interfaces.PositionService;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    public PositionServiceImpl(PositionRepository positionRepository, DepartmentRepository departmentRepository) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.CREATED)
    public PositionResponse create(PositionRequest req) {
        if (positionRepository.existsByName(req.name())) {
            throw new PositionAlreadyExistsException();
        }

        Department department = departmentRepository.findById(req.departmentId()).orElseThrow(DepartmentNotFound::new);

        Position position = new Position(req.name(), department);

        return PositionResponse.from(positionRepository.save(position));
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(UUID id) {
        Position position = positionRepository.findById(id).orElseThrow(PositionNotFoundException::new);

        positionRepository.delete(position);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionResponse read(UUID id) {
        return PositionResponse.from(positionRepository.findById(id).orElseThrow(PositionNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PositionResponse> readAll(Pageable pageable) {
        return positionRepository.findAll(pageable).map(PositionResponse::from);
    }

    @Override
    @Transactional
    public PositionResponse update(UUID id, PositionRequest req) {
        Position position = positionRepository.findById(id).orElseThrow(PositionNotFoundException::new);

        if (positionRepository.existsByName(req.name())) {
            throw new PositionAlreadyExistsException();
        }

        Department department = departmentRepository.findById(req.departmentId()).orElseThrow(DepartmentNotFound::new);

        position.setName(req.name());
        position.setDepartment(department);

        return PositionResponse.from(positionRepository.save(position));
    }

}
