package com.povilas.eagle_bank.transaction.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTransactionRequest(
        @NotNull BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String type,
        String reference
) {
}
