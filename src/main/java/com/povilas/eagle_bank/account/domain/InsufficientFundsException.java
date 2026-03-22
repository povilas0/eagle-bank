package com.povilas.eagle_bank.account.domain;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String accountNumber) {
        super("Insufficient funds for account: " + accountNumber);
    }
}
