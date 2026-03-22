package com.povilas.eagle_bank.account.domain;

public record UpdateAccountCommand(
        String name,
        String accountType
) {
}
