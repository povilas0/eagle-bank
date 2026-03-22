package com.povilas.eagle_bank.transaction.domain;

public interface TransactionRepository {
    void save(Transaction transaction);
}
