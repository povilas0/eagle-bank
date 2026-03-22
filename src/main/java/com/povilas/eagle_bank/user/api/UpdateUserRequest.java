package com.povilas.eagle_bank.user.api;

import jakarta.validation.Valid;

public record UpdateUserRequest(
        String name,
        @Valid AddressRequest address,
        String phoneNumber,
        String email
) {}
