package com.povilas.eagle_bank.auth.domain;

import com.povilas.eagle_bank.common.security.JwtService;
import com.povilas.eagle_bank.user.domain.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginCommand command) {
        var user = userService.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(command.password(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }
        return jwtService.generateToken(user);
    }

    public Optional<String> verifyToken(String token) {
        return jwtService.extractUserId(token);
    }
}
