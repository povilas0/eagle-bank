package com.povilas.eagle_bank.account.domain;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public void deleteAccount(DeleteAccountCommand command) {
        accountRepository.findByAccountNumber(command.accountNumber())
                .orElseThrow(() -> new AccountNotFoundException(command.accountNumber()));
        accountRepository.delete(command.accountNumber());
    }

    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        accountRepository.update(account.deposit(amount));
    }

    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        accountRepository.update(account.withdraw(amount));
    }

    public Account updateAccount(String accountNumber, UpdateAccountCommand command) {
        Account existing = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        Account updated = existing.withUpdates(command);
        accountRepository.update(updated);
        return updated;
    }
}
