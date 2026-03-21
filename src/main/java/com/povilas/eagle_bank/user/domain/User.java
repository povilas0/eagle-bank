package com.povilas.eagle_bank.user.domain;

import java.time.Instant;

public record User(
        String id,
        String name,
        Address address,
        String phoneNumber,
        String email,
        Instant createdTimestamp,
        Instant updatedTimestamp
) {}
