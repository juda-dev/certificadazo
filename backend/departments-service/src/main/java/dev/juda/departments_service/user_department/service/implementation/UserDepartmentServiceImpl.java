package dev.juda.departments_service.user_department.service.implementation;

import dev.juda.departments_service.user_department.presentation.dto.in.UserFullNameView;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClient;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.persistence.repository.DepartmentRepository;
import dev.juda.departments_service.department.presentation.exception.DepartmentNotFoundException;
import dev.juda.departments_service.position.persistence.entity.Position;
import dev.juda.departments_service.position.persistence.repository.PositionRepository;
import dev.juda.departments_service.position.presentation.exception.PositionNotFoundException;
import dev.juda.departments_service.user_department.persistence.embeddable.UserDepartmentId;
import dev.juda.departments_service.user_department.persistence.entity.UserDepartment;
import dev.juda.departments_service.user_department.persistence.repository.UserDepartmentRepository;
import dev.juda.departments_service.user_department.presentation.dto.request.DeleteUserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.request.UserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.response.UserDepartmentResponse;
import dev.juda.departments_service.user_department.presentation.exception.UserDepartmentNotFoundException;
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
        UserFullNameView userFullNameView = fetchUserFullName(req.userId());

        return persistUserDepartment(req, userFullNameView);
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(DeleteUserDepartmentRequest req) {
        UserDepartment userDepartment = userDepartmentRepository.findById_UserIdAndId_DepartmentId(
                req.userId(), req.departmentId()).orElseThrow(UserDepartmentNotFoundException::new);

        userDepartmentRepository.delete(userDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> departmentsPerUser(UUID userId) {
        return userDepartmentRepository.findById_UserId(userId)
                .stream()
                .map(
                        us -> departmentRepository
                                .findById(us.getId()
                                        .departmentId())
                                .orElseThrow(DepartmentNotFoundException::new))
                .toList();
    }

    @Override
    @Transactional
    public UserDepartmentResponse update(UserDepartmentRequest req) {
        UserDepartmentId userDepartmentId = new UserDepartmentId(req.userId(), req.departmentId(), req.positionId());

        UserDepartment userDepartment = userDepartmentRepository.findById(userDepartmentId)
                .orElseThrow(UserDepartmentNotFoundException::new);

        if (!positionRepository.existsById(req.positionId())) {
            throw new PositionNotFoundException();
        }

        userDepartmentRepository.delete(userDepartment);

        UserFullNameView userFullNameView = fetchUserFullName(req.userId());

        return persistUserDepartment(req, userFullNameView);
    }

    private UserDepartmentResponse persistUserDepartment(UserDepartmentRequest req, UserFullNameView userFullNameView) {
        Department department = departmentRepository.findById(req.departmentId())
                .orElseThrow(DepartmentNotFoundException::new);
        Position position = positionRepository.findById(req.positionId())
                .orElseThrow(PositionNotFoundException::new);

        UserDepartmentId userDepartmentId = new UserDepartmentId(req.userId(), req.departmentId(), req.positionId());
        userDepartmentRepository.save(new UserDepartment(userDepartmentId));

        return new UserDepartmentResponse(userFullNameView.fullName(), department.getName(), position.getName());
    }

    private UserFullNameView fetchUserFullName(UUID userId) {
        return restClient.get()
                .uri("/users/user-fullname-view/{id}", userId)
                .retrieve()
                .body(UserFullNameView.class);
    }
}
