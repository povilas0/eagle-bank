package com.povilas.eagle_bank.account.api;

public record UpdateAccountRequest(
        String name,
        String accountType
) {
}
