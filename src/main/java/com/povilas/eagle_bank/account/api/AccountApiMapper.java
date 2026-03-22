package com.povilas.eagle_bank.account.api;

import com.povilas.eagle_bank.account.domain.Account;
import com.povilas.eagle_bank.account.domain.CreateAccountCommand;
import com.povilas.eagle_bank.account.domain.DeleteAccountCommand;
import com.povilas.eagle_bank.account.domain.UpdateAccountCommand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountApiMapper {

    public CreateAccountCommand toCreateCommand(CreateAccountRequest request) {
        return new CreateAccountCommand(request.userId(), request.name(), request.accountType());
    }

    public UpdateAccountCommand toUpdateCommand(UpdateAccountRequest request) {
        return new UpdateAccountCommand(request.name(), request.accountType());
    }

    public DeleteAccountCommand toDeleteCommand(String accountNumber) {
        return new DeleteAccountCommand(accountNumber);
    }

    public ListAccountsResponse toListResponse(List<Account> accounts) {
        return new ListAccountsResponse(accounts.stream().map(this::toResponse).toList());
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
