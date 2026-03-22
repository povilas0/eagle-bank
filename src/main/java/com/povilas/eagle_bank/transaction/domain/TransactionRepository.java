package com.povilas.eagle_bank.transaction.domain;

import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);
    List<Transaction> findByAccountNumber(String accountNumber);
}
