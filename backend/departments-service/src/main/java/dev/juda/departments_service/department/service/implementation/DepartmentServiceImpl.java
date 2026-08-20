package dev.juda.departments_service.department.service.implementation;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.persistence.repository.DepartmentRepository;
import dev.juda.departments_service.department.presentation.dto.request.DepartmentRequest;
import dev.juda.departments_service.department.presentation.dto.response.DepartmentResponse;
import dev.juda.departments_service.department.service.exception.DepartmentAlreadyExistsException;
import dev.juda.departments_service.department.service.exception.DepartmentNotFoundException;
import dev.juda.departments_service.department.service.interfaces.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private static final Logger LOG = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    public DepartmentServiceImpl(DepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.CREATED)
    public Department create(DepartmentRequest req) {
        LOG.trace("Starting department creation {}", req.name());

        if (repository.existsByName(req.name())) {
            LOG.warn("Department with name {} already exists", req.name());
            throw new DepartmentAlreadyExistsException();
        }

        LOG.info("Ending department creation {}", req.name());
        return repository.save(new Department(req.name()));
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(UUID id) {
        LOG.trace("Starting department deletion {}", id);
        Department department = repository.findById(id).orElseThrow(DepartmentNotFoundException::new);

        LOG.info("Ending department deletion {}", id);
        repository.delete(department);
    }

    @Override
    @Transactional
    public Department update(UUID id, DepartmentRequest req) {
        LOG.trace("Starting department update {}", id);
        Department department = repository.findById(id).orElseThrow(DepartmentNotFoundException::new);

        if (repository.existsByName(req.name())) {
            LOG.warn("Department with name {} already exists", req.name());
            throw new DepartmentAlreadyExistsException();
        }

        department.setName(req.name());

        LOG.info("Ending department update {}", id);
        return repository.save(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse read(UUID id) {
        return repository.findById(id)
                .map(DepartmentResponse::from)
                .orElseThrow(DepartmentNotFoundException::new);
    }

    @Override
    public Boolean existsById(UUID id) {
        return repository.existsById(id);
    }

}
