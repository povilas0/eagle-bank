package com.povilas.eagle_bank.account.domain;

public enum AccountType {
    PERSONAL("personal");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AccountType fromValue(String value) {
        for (AccountType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new InvalidAccountTypeException(value);
    }
}
