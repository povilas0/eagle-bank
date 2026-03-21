package com.povilas.eagle_bank.user.web;

import java.time.Instant;

public record UserResponse(
        String id,
        String name,
        AddressResponse address,
        String phoneNumber,
        String email,
        Instant createdTimestamp,
        Instant updatedTimestamp
) {}
