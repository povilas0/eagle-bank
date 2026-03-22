package com.povilas.eagle_bank.account.api;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(
        String accountNumber,
        String sortCode,
        String name,
        String accountType,
        BigDecimal balance,
        String currency,
        Instant createdTimestamp,
        Instant updatedTimestamp
) {}
