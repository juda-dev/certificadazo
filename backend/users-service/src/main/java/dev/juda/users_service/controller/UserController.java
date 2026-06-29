package dev.juda.users_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.users_service.models.dto.request.CreateUserRequest;
import dev.juda.users_service.models.dto.response.CreateUserResponse;
import dev.juda.users_service.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public CreateUserResponse create(@Valid @RequestBody CreateUserRequest req){
        return userService.create(req);
    }
}
