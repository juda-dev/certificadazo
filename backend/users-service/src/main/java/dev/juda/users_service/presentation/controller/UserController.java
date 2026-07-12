package dev.juda.users_service.presentation.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.users_service.messaging.dto.in.Reply;
import dev.juda.users_service.presentation.dto.request.CreateUserRequest;
import dev.juda.users_service.presentation.dto.request.PasswordChangeRequest;
import dev.juda.users_service.presentation.dto.request.UpdateUserRequest;
import dev.juda.users_service.presentation.dto.response.UserFullNameView;
import dev.juda.users_service.presentation.dto.response.UserResponse;
import dev.juda.users_service.service.interfaces.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest req) {
        return userService.create(req);
    }

    @PutMapping("/update/{id}")
    public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest req) {
        return userService.update(id, req);
    }

    @PutMapping("/update/password/{id}")
    public ResponseEntity<Reply<?>> updatePassword(@PathVariable UUID id,
            @Valid @RequestBody PasswordChangeRequest req) {
        return userService.updatePassword(id, req);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @GetMapping("/user-fullname-view/{id}")
    public UserFullNameView userFullNameView(@PathVariable UUID id) {
        return userService.userFullNameView(id);
    }

    @GetMapping("/find-id/document-id/{documentId}")
    public UUID findByDocumentId(@PathVariable String documentId) {
        return userService.findIdByDocumentId(documentId);
    }

    @GetMapping("/find-id/email/{email}")
    public UUID findByEmail(@PathVariable String email) {
        return userService.findIdByEmail(email);
    }

    @GetMapping("/exists/{id}")
    public Boolean existsById(@PathVariable UUID id) {
        return userService.existsById(id);
    }
}
