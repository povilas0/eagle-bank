package com.povilas.eagle_bank.transaction.api;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        String id,
        BigDecimal amount,
        String currency,
        String type,
        String reference,
        Instant createdTimestamp
) {
}
