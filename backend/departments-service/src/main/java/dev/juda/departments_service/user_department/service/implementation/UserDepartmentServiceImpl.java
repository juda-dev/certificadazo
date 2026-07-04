package dev.juda.departments_service.user_department.service.implementation;

import dev.juda.departments_service.user_department.presentation.dto.in.UserFullNameView;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.persistence.repository.DepartmentRepository;
import dev.juda.departments_service.department.presentation.exception.DepartmentNotFound;
import dev.juda.departments_service.position.persistence.entity.Position;
import dev.juda.departments_service.position.persistence.repository.PositionRepository;
import dev.juda.departments_service.position.presentation.exception.PositionNotFoundException;
import dev.juda.departments_service.user_department.persistence.embeddable.UserDepartmentId;
import dev.juda.departments_service.user_department.persistence.entity.UserDepartment;
import dev.juda.departments_service.user_department.persistence.repository.UserDepartmentRepository;
import dev.juda.departments_service.user_department.presentation.dto.request.DeleteUserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.request.UserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.response.UserDepartmentResponse;
import dev.juda.departments_service.user_department.service.interfaces.UserDepartmentService;

@Service
public class UserDepartmentServiceImpl implements UserDepartmentService {

    private final UserDepartmentRepository userDepartmentRepository;
    private final RestClient restClient;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public UserDepartmentServiceImpl(UserDepartmentRepository userDepartmentRepository, RestClient restClient,
            DepartmentRepository departmentRepository,
            PositionRepository positionRepository) {
        this.userDepartmentRepository = userDepartmentRepository;
        this.restClient = restClient;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    @Transactional
    public UserDepartmentResponse create(UserDepartmentRequest req) {
        UserFullNameView userFullNameView = restClient.get()
                .uri("/users/user-fullname-view/{id}", req.userId())
                .retrieve()
                .body(UserFullNameView.class);

        Department department = departmentRepository.findById(req.departmentId()).orElseThrow(DepartmentNotFound::new);

        Position position = positionRepository.findById(req.positionId()).orElseThrow(PositionNotFoundException::new);

        UserDepartmentId userDepartmentId = new UserDepartmentId(req.userId(), req.departmentId(), req.positionId());

        userDepartmentRepository.save(new UserDepartment(userDepartmentId));

        return new UserDepartmentResponse(userFullNameView.fullName(), department.getName(), position.getName());
    }

    @Override
    public void delete(DeleteUserDepartmentRequest req) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Department> departmentsPerUser(UUID userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserDepartmentResponse update(UserDepartmentRequest req) {
        // TODO Auto-generated method stub
        return null;
    }

}
