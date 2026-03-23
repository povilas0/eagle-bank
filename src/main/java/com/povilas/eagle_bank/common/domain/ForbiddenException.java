package com.povilas.eagle_bank.common.domain;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Access denied");
    }
}
