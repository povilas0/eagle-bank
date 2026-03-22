package com.povilas.eagle_bank.account.api;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank String userId,
        @NotBlank String name,
        @NotBlank String accountType
) {}
