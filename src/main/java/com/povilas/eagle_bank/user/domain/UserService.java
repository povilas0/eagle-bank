package com.povilas.eagle_bank.user.domain;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserCommand command) {
        String passwordHash = passwordEncoder.encode(command.password());
        User user = new User(command, passwordHash);
        userRepository.save(user);
        return user;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(String userId, UpdateUserCommand command) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User updated = existing.withUpdates(command);
        userRepository.update(updated);
        return updated;
    }

    public void deleteUser(DeleteUserCommand command) {
        userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        userRepository.delete(command.userId());
    }
}
