package com.povilas.eagle_bank.account.domain;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;

@Service
public class AccountService {

    private static final String SORT_CODE = "10-10-10";
    private static final String CURRENCY = "GBP";
    private static final Random RANDOM = new Random();

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(CreateAccountCommand command) {
        String accountNumber = "01" + String.format("%06d", RANDOM.nextInt(1_000_000));
        Instant now = Instant.now();
        AccountType accountType = AccountType.fromValue(command.accountType());
        Account account = new Account(
                accountNumber,
                SORT_CODE,
                command.userId(),
                command.name(),
                accountType,
                BigDecimal.ZERO,
                CURRENCY,
                now,
                now
        );
        accountRepository.save(account);
        return account;
    }
}
