package com.povilas.eagle_bank.account.domain;

import java.util.List;

public interface AccountRepository {
    void save(Account account);
    List<Account> findByUserId(String userId);
}
