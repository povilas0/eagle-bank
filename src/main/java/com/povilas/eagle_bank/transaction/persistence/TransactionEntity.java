package com.povilas.eagle_bank.transaction.persistence;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record TransactionEntity(
        String id,
        String accountNumber,
        BigDecimal amount,
        String currency,
        String type,
        String reference,
        Timestamp createdTimestamp
) {
}
