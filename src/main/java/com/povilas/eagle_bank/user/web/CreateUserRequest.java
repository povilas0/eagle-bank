package com.povilas.eagle_bank.user.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank String name,
        @NotNull @Valid AddressRequest address,
        @NotBlank String phoneNumber,
        @NotBlank String email
) {}
