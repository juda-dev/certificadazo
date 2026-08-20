package dev.juda.departments_service.position.service.implementation;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.persistence.repository.DepartmentRepository;
import dev.juda.departments_service.department.service.exception.DepartmentNotFoundException;
import dev.juda.departments_service.position.persistence.entity.Position;
import dev.juda.departments_service.position.persistence.repository.PositionRepository;
import dev.juda.departments_service.position.presentation.dto.request.PositionRequest;
import dev.juda.departments_service.position.presentation.dto.response.PositionResponse;
import dev.juda.departments_service.position.service.exception.PositionAlreadyExistsException;
import dev.juda.departments_service.position.service.exception.PositionNotFoundException;
import dev.juda.departments_service.position.service.interfaces.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

    public PositionServiceImpl(PositionRepository positionRepository, DepartmentRepository departmentRepository) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.CREATED)
    public PositionResponse create(PositionRequest req) {
        LOG.trace("Starting creation of position {} in department {}", req.name(), req.departmentId());
        if (positionRepository.existsByName(req.name())) {
            LOG.warn("Position with name {} already exists", req.name());
            throw new PositionAlreadyExistsException();
        }

        LOG.trace("Ending creation of position {}", req.name());
        return PositionResponse.from(positionRepository.save(new Position(req.name(), departmentRepository.findById(req.departmentId()).orElseThrow(DepartmentNotFoundException::new))));
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(UUID id) {
        LOG.trace("Starting deletion of position {}", id);
        Position position = positionRepository.findById(id).orElseThrow(PositionNotFoundException::new);

        LOG.info("The removal of the position is complete {}", id);
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
        LOG.trace("Starting update of position {} ", id);

        Position position = positionRepository.findById(id).orElseThrow(PositionNotFoundException::new);

        if (positionRepository.existsByName(req.name())) {
            LOG.warn("Position with name {} already exists", req.name());
            throw new PositionAlreadyExistsException();
        }

        Department department = departmentRepository.findById(req.departmentId()).orElseThrow(DepartmentNotFoundException::new);

        position.setName(req.name());
        position.setDepartment(department);

        LOG.info("The position update is complete {}", id);

        return PositionResponse.from(positionRepository.save(position));
    }

}
