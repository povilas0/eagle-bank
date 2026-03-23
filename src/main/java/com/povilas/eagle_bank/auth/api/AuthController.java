package com.povilas.eagle_bank.auth.api;

import com.povilas.eagle_bank.auth.domain.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthApiMapper mapper;

    public AuthController(AuthService authService, AuthApiMapper mapper) {
        this.authService = authService;
        this.mapper = mapper;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return new LoginResponse(authService.login(mapper.toLoginCommand(request)));
    }
}
