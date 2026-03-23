package com.povilas.eagle_bank.user.domain;

public record CreateUserCommand(
        String name,
        Address address,
        String phoneNumber,
        String email,
        String password
) {}
