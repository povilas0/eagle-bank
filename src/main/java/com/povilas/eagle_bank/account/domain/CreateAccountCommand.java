package com.povilas.eagle_bank.account.domain;

public record CreateAccountCommand(
        String userId,
        String name,
        String accountType
) {}
