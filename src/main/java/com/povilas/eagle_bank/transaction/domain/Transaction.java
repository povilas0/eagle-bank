package com.povilas.eagle_bank.transaction.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Transaction(
        String id,
        String accountNumber,
        BigDecimal amount,
        String currency,
        TransactionType type,
        String reference,
        Instant createdTimestamp
) {
    public Transaction(CreateTransactionCommand command) {
        this("tan-" + UUID.randomUUID().toString().replace("-", ""),
                command.accountNumber(),
                command.amount(),
                command.currency(),
                TransactionType.fromValue(command.type()),
                command.reference(),
                Instant.now());
    }
}
