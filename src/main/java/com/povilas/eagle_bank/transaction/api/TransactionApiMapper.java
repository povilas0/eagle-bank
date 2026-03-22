package com.povilas.eagle_bank.transaction.api;

import com.povilas.eagle_bank.transaction.domain.CreateTransactionCommand;
import com.povilas.eagle_bank.transaction.domain.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionApiMapper {

    public CreateTransactionCommand toCreateCommand(String accountNumber, CreateTransactionRequest request) {
        return new CreateTransactionCommand(
                accountNumber,
                request.amount(),
                request.currency(),
                request.type(),
                request.reference()
        );
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.id(),
                transaction.amount(),
                transaction.currency(),
                transaction.type().getValue(),
                transaction.reference(),
                transaction.createdTimestamp()
        );
    }
}
