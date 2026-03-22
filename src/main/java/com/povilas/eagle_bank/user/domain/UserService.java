package com.povilas.eagle_bank.user.domain;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User updateUser(String userId, UpdateUserCommand command) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        User updated = new User(
                existing.id(),
                command.name() != null ? command.name() : existing.name(),
                command.address() != null ? command.address() : existing.address(),
                command.phoneNumber() != null ? command.phoneNumber() : existing.phoneNumber(),
                command.email() != null ? command.email() : existing.email(),
                existing.createdTimestamp(),
                Instant.now()
        );
        userRepository.update(updated);
        return updated;
    }

    public User createUser(CreateUserCommand command) {
        String id = "usr-" + UUID.randomUUID().toString().replace("-", "");
        Instant now = Instant.now();
        User user = new User(id, command.name(), command.address(), command.phoneNumber(), command.email(), now, now);
        userRepository.save(user);
        return user;
    }
}
