package com.povilas.eagle_bank.transaction.domain;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);
    Optional<Transaction> findById(String transactionId);
    List<Transaction> findByAccountNumber(String accountNumber);
}
