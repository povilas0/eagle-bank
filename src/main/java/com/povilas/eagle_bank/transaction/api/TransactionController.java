package com.povilas.eagle_bank.transaction.api;

import com.povilas.eagle_bank.transaction.domain.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionApiMapper mapper;

    public TransactionController(TransactionService transactionService, TransactionApiMapper mapper) {
        this.transactionService = transactionService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@PathVariable String accountNumber,
                                                 @Valid @RequestBody CreateTransactionRequest request) {
        return mapper.toResponse(transactionService.createTransaction(mapper.toCreateCommand(accountNumber, request)));
    }
}
