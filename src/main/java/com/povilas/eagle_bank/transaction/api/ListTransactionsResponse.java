package com.povilas.eagle_bank.transaction.api;

import java.util.List;

public record ListTransactionsResponse(List<TransactionResponse> transactions) {
}
