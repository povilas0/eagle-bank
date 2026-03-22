package com.povilas.eagle_bank.transaction.domain;

public class InvalidTransactionTypeException extends RuntimeException {

    public InvalidTransactionTypeException(String value) {
        super("Invalid transaction type: " + value);
    }
}
