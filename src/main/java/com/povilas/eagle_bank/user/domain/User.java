package com.povilas.eagle_bank.user.domain;

import java.time.Instant;
import java.util.UUID;

public record User(
        String id,
        String name,
        Address address,
        String phoneNumber,
        String email,
        Instant createdTimestamp,
        Instant updatedTimestamp
) {
    public User(CreateUserCommand command) {
        this("usr-" + UUID.randomUUID().toString().replace("-", ""),
                command.name(), command.address(), command.phoneNumber(), command.email(),
                Instant.now(), Instant.now());
    }

    public User withUpdates(UpdateUserCommand command) {
        return new User(
                id,
                command.name() != null ? command.name() : name,
                command.address() != null ? command.address() : address,
                command.phoneNumber() != null ? command.phoneNumber() : phoneNumber,
                command.email() != null ? command.email() : email,
                createdTimestamp,
                Instant.now()
        );
    }
}
