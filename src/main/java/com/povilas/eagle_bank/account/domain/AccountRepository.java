package com.povilas.eagle_bank.account.domain;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByUserId(String userId);
}
