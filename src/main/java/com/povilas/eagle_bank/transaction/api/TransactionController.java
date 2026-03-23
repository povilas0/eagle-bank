package com.povilas.eagle_bank.transaction.api;

import com.povilas.eagle_bank.transaction.domain.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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

    @GetMapping
    public ListTransactionsResponse listTransactions(@PathVariable String accountNumber,
                                                     Authentication authentication) {
        return mapper.toListResponse(transactionService.listTransactions(accountNumber, (String) authentication.getPrincipal()));
    }

    @GetMapping("/{transactionId}")
    public TransactionResponse getTransaction(@PathVariable String accountNumber,
                                              @PathVariable String transactionId,
                                              Authentication authentication) {
        return mapper.toResponse(transactionService.getTransaction(accountNumber, transactionId, (String) authentication.getPrincipal()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@PathVariable String accountNumber,
                                                 @Valid @RequestBody CreateTransactionRequest request,
                                                 Authentication authentication) {
        return mapper.toResponse(transactionService.createTransaction(mapper.toCreateCommand(accountNumber, (String) authentication.getPrincipal(), request)));
    }
}
