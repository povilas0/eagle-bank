package com.povilas.eagle_bank.account.api;

import java.util.List;

public record ListAccountsResponse(
        List<AccountResponse> accounts
) {}
