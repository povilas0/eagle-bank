package com.povilas.eagle_bank.user.domain;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(String id);
}
