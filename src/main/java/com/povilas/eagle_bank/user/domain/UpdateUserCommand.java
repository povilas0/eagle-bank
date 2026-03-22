package com.povilas.eagle_bank.user.domain;

public record UpdateUserCommand(
        String name,
        Address address,
        String phoneNumber,
        String email
) {}
