package dev.juda.departments_service.department.service.implementation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.persistence.repository.DepartmentRepository;
import dev.juda.departments_service.department.presentation.dto.request.DepartmentRequest;
import dev.juda.departments_service.department.presentation.exception.DepartmentAlreadyExistsException;
import dev.juda.departments_service.department.presentation.exception.DepartmentNotFound;
import dev.juda.departments_service.department.service.interfaces.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentServiceImpl(DepartmentRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.CREATED)
    public Department create(DepartmentRequest req) {
        if (repository.existsByName(req.name())) {
            throw new DepartmentAlreadyExistsException();
        }

        return repository.save(new Department(req.name()));
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(UUID id) {
        Department department = repository.findById(id).orElseThrow(DepartmentNotFound::new);

        repository.delete(department);
    }

    @Override
    @Transactional
    public Department update(UUID id, DepartmentRequest req) {
        Department department = repository.findById(id).orElseThrow(DepartmentNotFound::new);

        if (repository.existsByName(req.name())) {
            throw new DepartmentAlreadyExistsException();
        }

        department.setName(req.name());

        return repository.save(department);
    }

}
