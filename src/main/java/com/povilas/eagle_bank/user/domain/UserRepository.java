package com.povilas.eagle_bank.user.domain;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    void update(User user);
    void delete(String userId);
    Optional<User> findById(String id);
}
