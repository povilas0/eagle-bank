package com.povilas.eagle_bank.user.domain;

public class UserHasAccountsException extends RuntimeException {
    public UserHasAccountsException(String userId) {
        super("User " + userId + " still has active bank accounts");
    }
}
