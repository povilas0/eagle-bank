package com.povilas.eagle_bank.account.domain;

import java.math.BigDecimal;
import java.time.Instant;

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
) {}
