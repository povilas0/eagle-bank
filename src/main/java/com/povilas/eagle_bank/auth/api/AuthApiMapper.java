package com.povilas.eagle_bank.auth.api;

import com.povilas.eagle_bank.auth.domain.LoginCommand;
import org.springframework.stereotype.Component;

@Component
public class AuthApiMapper {

    public LoginCommand toLoginCommand(LoginRequest request) {
        return new LoginCommand(request.email(), request.password());
    }
}
