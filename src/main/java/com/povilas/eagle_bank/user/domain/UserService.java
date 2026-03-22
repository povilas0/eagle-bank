package com.povilas.eagle_bank.user.domain;

import org.springframework.stereotype.Service;

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
        User updated = existing.withUpdates(command);
        userRepository.update(updated);
        return updated;
    }

    public void deleteUser(DeleteUserCommand command) {
        userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        userRepository.delete(command.userId());
    }

    public User createUser(CreateUserCommand command) {
        User user = new User(command);
        userRepository.save(user);
        return user;
    }
}
