package com.povilas.eagle_bank.account.api;

import com.povilas.eagle_bank.account.domain.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountApiMapper mapper;

    public AccountController(AccountService accountService, AccountApiMapper mapper) {
        this.accountService = accountService;
        this.mapper = mapper;
    }

    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(@PathVariable String accountNumber, Authentication authentication) {
        return mapper.toResponse(accountService.getAccount(accountNumber, (String) authentication.getPrincipal()));
    }

    @GetMapping
    public ListAccountsResponse listAccounts(@RequestParam String userId) {
        return mapper.toListResponse(accountService.listAccounts(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return mapper.toResponse(accountService.createAccount(mapper.toCreateCommand(request)));
    }

    @PatchMapping("/{accountNumber}")
    public AccountResponse updateAccount(@PathVariable String accountNumber,
                                         @RequestBody UpdateAccountRequest request,
                                         Authentication authentication) {
        return mapper.toResponse(accountService.updateAccount(accountNumber, (String) authentication.getPrincipal(), mapper.toUpdateCommand(request)));
    }

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable String accountNumber, Authentication authentication) {
        accountService.deleteAccount(mapper.toDeleteCommand(accountNumber), (String) authentication.getPrincipal());
    }
}
