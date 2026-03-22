package com.povilas.eagle_bank.user.web;

import jakarta.validation.Valid;

public record UpdateUserRequest(
        String name,
        @Valid AddressRequest address,
        String phoneNumber,
        String email
) {}
