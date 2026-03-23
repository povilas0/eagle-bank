package com.povilas.eagle_bank.account.domain;

import com.povilas.eagle_bank.common.domain.ForbiddenException;
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

    public Account getAccount(String accountNumber, String authenticatedUserId) {
        Account account = getAccount(accountNumber);
        if (!account.userId().equals(authenticatedUserId)) {
            throw new ForbiddenException();
        }
        return account;
    }

    public List<Account> listAccounts(String userId) {
        return accountRepository.findByUserId(userId);
    }

    public void deleteAccount(DeleteAccountCommand command, String authenticatedUserId) {
        Account account = accountRepository.findByAccountNumber(command.accountNumber())
                .orElseThrow(() -> new AccountNotFoundException(command.accountNumber()));
        if (!account.userId().equals(authenticatedUserId)) {
            throw new ForbiddenException();
        }
        accountRepository.delete(command.accountNumber());
    }

    public void deposit(String accountNumber, String authenticatedUserId, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        if (!account.userId().equals(authenticatedUserId)) {
            throw new ForbiddenException();
        }
        accountRepository.update(account.deposit(amount));
    }

    public void withdraw(String accountNumber, String authenticatedUserId, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        if (!account.userId().equals(authenticatedUserId)) {
            throw new ForbiddenException();
        }
        accountRepository.update(account.withdraw(amount));
    }

    public Account updateAccount(String accountNumber, String authenticatedUserId, UpdateAccountCommand command) {
        Account existing = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        if (!existing.userId().equals(authenticatedUserId)) {
            throw new ForbiddenException();
        }
        Account updated = existing.withUpdates(command);
        accountRepository.update(updated);
        return updated;
    }
}
