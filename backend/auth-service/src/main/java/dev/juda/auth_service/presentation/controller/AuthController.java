package dev.juda.auth_service.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.juda.auth_service.presentation.dto.request.AuthRequest;
import dev.juda.auth_service.presentation.dto.response.AuthResponse;
import dev.juda.auth_service.service.interfaces.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req){
        return authService.login(req);
    }
}
