package com.povilas.eagle_bank.account.domain;

public class InvalidAccountTypeException extends RuntimeException {
    public InvalidAccountTypeException(String value) {
        super("Invalid account type: " + value);
    }
}
