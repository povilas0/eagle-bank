package com.povilas.eagle_bank.transaction.domain;

import com.povilas.eagle_bank.account.domain.AccountService;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public Transaction createTransaction(CreateTransactionCommand command) {
        Transaction transaction = new Transaction(command);

        switch (transaction.type()) {
            case DEPOSIT -> accountService.deposit(command.accountNumber(), command.amount());
            case WITHDRAWAL -> accountService.withdraw(command.accountNumber(), command.amount());
        }

        transactionRepository.save(transaction);
        return transaction;
    }
}
