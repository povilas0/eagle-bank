package com.povilas.eagle_bank.transaction.domain;

import java.math.BigDecimal;

public record CreateTransactionCommand(
        String accountNumber,
        BigDecimal amount,
        String currency,
        String type,
        String reference
) {
}
