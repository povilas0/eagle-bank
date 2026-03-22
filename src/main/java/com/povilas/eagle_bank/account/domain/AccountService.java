package com.povilas.eagle_bank.account.domain;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountCommand command) {
        Account account = new Account(command);
        accountRepository.save(account);
        return account;
    }
}
