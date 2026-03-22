package com.povilas.eagle_bank.account.domain;

import org.springframework.stereotype.Service;

import java.util.List;

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

    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    public List<Account> listAccounts(String userId) {
        return accountRepository.findByUserId(userId);
    }
}
