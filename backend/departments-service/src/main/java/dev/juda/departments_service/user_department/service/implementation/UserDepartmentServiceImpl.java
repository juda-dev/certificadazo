package dev.juda.departments_service.user_department.service.implementation;

import dev.juda.departments_service.department.persistence.entity.Department;
import dev.juda.departments_service.department.persistence.repository.DepartmentRepository;
import dev.juda.departments_service.department.service.exception.DepartmentNotFoundException;
import dev.juda.departments_service.position.persistence.entity.Position;
import dev.juda.departments_service.position.persistence.repository.PositionRepository;
import dev.juda.departments_service.position.service.exception.PositionNotFoundException;
import dev.juda.departments_service.user_department.persistence.embeddable.UserDepartmentId;
import dev.juda.departments_service.user_department.persistence.entity.UserDepartment;
import dev.juda.departments_service.user_department.persistence.repository.UserDepartmentRepository;
import dev.juda.departments_service.user_department.presentation.dto.in.UserFullNameView;
import dev.juda.departments_service.user_department.presentation.dto.request.DeleteUserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.request.UserDepartmentRequest;
import dev.juda.departments_service.user_department.presentation.dto.response.UserDepartmentResponse;
import dev.juda.departments_service.user_department.service.exception.UserDepartmentAlreadyExistsException;
import dev.juda.departments_service.user_department.service.exception.UserDepartmentNotFoundException;
import dev.juda.departments_service.user_department.service.interfaces.UserDepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
public class UserDepartmentServiceImpl implements UserDepartmentService {

    private final UserDepartmentRepository userDepartmentRepository;
    private final RestClient restClient;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserDepartmentServiceImpl.class);

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
        LOG.trace("Starting user creation {} with position {} in department {}", req.userId(), req.positionId(), req.departmentId());

        if (userDepartmentRepository.existsById_UserIdAndId_DepartmentId(req.userId(), req.departmentId())) {
            LOG.warn("User {} already exists in department {}", req.userId(), req.departmentId());
            throw new UserDepartmentAlreadyExistsException();
        }

        UserFullNameView userFullNameView = fetchUserFullName(req.userId());

        LOG.info("User creation {} with position {} of department {} completed",  req.userId(), req.positionId(), req.departmentId());
        return persistUserDepartment(req, userFullNameView);
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(DeleteUserDepartmentRequest req) {
        LOG.trace("Starting user deletion {} in department {}", req.userId(), req.departmentId());

        UserDepartment userDepartment = userDepartmentRepository.findById_UserIdAndId_DepartmentId(
                req.userId(), req.departmentId()).orElseThrow(UserDepartmentNotFoundException::new);

        LOG.info("User deletion {} in department {}", req.userId(), req.departmentId());
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
        LOG.trace("Starting user department update {}",  req.userId());
        UserDepartmentId userDepartmentId = new UserDepartmentId(req.userId(), req.departmentId(), req.positionId());

        UserDepartment userDepartment = userDepartmentRepository.findById(userDepartmentId)
                .orElseThrow(UserDepartmentNotFoundException::new);

        if (!positionRepository.existsById(req.positionId())) {
            throw new PositionNotFoundException();
        }

        userDepartmentRepository.delete(userDepartment);

        UserFullNameView userFullNameView = fetchUserFullName(req.userId());

        LOG.info("User's department {} was updated", req.userId());
        return persistUserDepartment(req, userFullNameView);
    }

    private UserDepartmentResponse persistUserDepartment(UserDepartmentRequest req, UserFullNameView userFullNameView) {
        LOG.trace("Starting user department persistent {}",  req.userId());
        Department department = departmentRepository.findById(req.departmentId())
                .orElseThrow(DepartmentNotFoundException::new);
        Position position = positionRepository.findById(req.positionId())
                .orElseThrow(PositionNotFoundException::new);

        UserDepartmentId userDepartmentId = new UserDepartmentId(req.userId(), req.departmentId(), req.positionId());
        userDepartmentRepository.save(new UserDepartment(userDepartmentId));

        LOG.info("User's department {} was persisted", req.userId());
        return new UserDepartmentResponse(userFullNameView.fullName(), department.getName(), position.getName());
    }

    private UserFullNameView fetchUserFullName(UUID userId) {
        return restClient.get()
                .uri("/users/user-fullname-view/{id}", userId)
                .retrieve()
                .body(UserFullNameView.class);
    }
}
