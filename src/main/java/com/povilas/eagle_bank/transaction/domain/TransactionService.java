package com.povilas.eagle_bank.transaction.domain;

import com.povilas.eagle_bank.account.domain.Account;
import com.povilas.eagle_bank.account.domain.AccountNotFoundException;
import com.povilas.eagle_bank.account.domain.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction createTransaction(CreateTransactionCommand command) {
        Account account = accountRepository.findByAccountNumber(command.accountNumber())
                .orElseThrow(() -> new AccountNotFoundException(command.accountNumber()));

        Transaction transaction = new Transaction(command);

        Account updatedAccount = switch (transaction.type()) {
            case DEPOSIT -> account.deposit(command.amount());
            case WITHDRAWAL -> account.withdraw(command.amount());
        };

        accountRepository.update(updatedAccount);
        transactionRepository.save(transaction);
        return transaction;
    }
}
