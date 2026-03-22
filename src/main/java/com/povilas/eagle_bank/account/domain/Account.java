package com.povilas.eagle_bank.account.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;

public record Account(
        String accountNumber,
        String sortCode,
        String userId,
        String name,
        AccountType accountType,
        BigDecimal balance,
        String currency,
        Instant createdTimestamp,
        Instant updatedTimestamp
) {
    private static final String SORT_CODE = "10-10-10";
    private static final String CURRENCY = "GBP";
    private static final Random RANDOM = new Random();

    public Account(CreateAccountCommand command) {
        this("01" + String.format("%06d", RANDOM.nextInt(1_000_000)),
                SORT_CODE,
                command.userId(),
                command.name(),
                AccountType.fromValue(command.accountType()),
                BigDecimal.ZERO,
                CURRENCY,
                Instant.now(),
                Instant.now());
    }

    public Account withUpdates(UpdateAccountCommand command) {
        return new Account(
                accountNumber,
                sortCode,
                userId,
                command.name() != null ? command.name() : name,
                command.accountType() != null ? AccountType.fromValue(command.accountType()) : accountType,
                balance,
                currency,
                createdTimestamp,
                Instant.now()
        );
    }
}
