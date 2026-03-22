package com.povilas.eagle_bank.account.api;

import com.povilas.eagle_bank.account.domain.Account;
import com.povilas.eagle_bank.account.domain.CreateAccountCommand;
import org.springframework.stereotype.Component;

@Component
public class AccountApiMapper {

    public CreateAccountCommand toCreateCommand(CreateAccountRequest request) {
        return new CreateAccountCommand(request.userId(), request.name(), request.accountType());
    }

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.accountNumber(),
                account.sortCode(),
                account.name(),
                account.accountType().getValue(),
                account.balance(),
                account.currency(),
                account.createdTimestamp(),
                account.updatedTimestamp()
        );
    }
}
