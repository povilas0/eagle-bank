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

    public User createUser(String name, Address address, String phoneNumber, String email) {
        String id = "usr-" + UUID.randomUUID().toString().replace("-", "");
        Instant now = Instant.now();
        User user = new User(id, name, address, phoneNumber, email, now, now);
        userRepository.save(user);
        return user;
    }
}
