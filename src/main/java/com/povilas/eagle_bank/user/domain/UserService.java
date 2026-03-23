package com.povilas.eagle_bank.user.domain;

import com.povilas.eagle_bank.account.domain.AccountService;
import com.povilas.eagle_bank.common.domain.ForbiddenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AccountService accountService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserCommand command) {
        String passwordHash = passwordEncoder.encode(command.password());
        User user = new User(command, passwordHash);
        userRepository.save(user);
        return user;
    }

    public User getUser(String userId, String authenticatedUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (!userId.equals(authenticatedUserId)) {
            throw new ForbiddenException();
        }
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(String userId, String authenticatedUserId, UpdateUserCommand command) {
        User existing = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (!userId.equals(authenticatedUserId)) {
            throw new ForbiddenException();
        }
        User updated = existing.withUpdates(command);
        userRepository.update(updated);
        return updated;
    }

    public void deleteUser(DeleteUserCommand command, String authenticatedUserId) {
        userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException(command.userId()));
        if (!command.userId().equals(authenticatedUserId)) {
            throw new ForbiddenException();
        }
        if (!accountService.listAccounts(command.userId()).isEmpty()) {
            throw new UserHasAccountsException(command.userId());
        }
        userRepository.delete(command.userId());
    }
}
