package com.povilas.eagle_bank.account.persistence;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record AccountEntity(
        String accountNumber,
        String sortCode,
        String userId,
        String name,
        String accountType,
        BigDecimal balance,
        String currency,
        Timestamp createdTimestamp,
        Timestamp updatedTimestamp
) {}
