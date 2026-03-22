package com.povilas.eagle_bank.account.persistence;

import com.povilas.eagle_bank.account.domain.Account;
import com.povilas.eagle_bank.account.domain.AccountType;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class AccountPersistenceMapper {

    public AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.accountNumber(),
                account.sortCode(),
                account.userId(),
                account.name(),
                account.accountType().getValue(),
                account.balance(),
                account.currency(),
                Timestamp.from(account.createdTimestamp()),
                Timestamp.from(account.updatedTimestamp())
        );
    }

    public AccountEntity toEntity(ResultSet rs) throws SQLException {
        return new AccountEntity(
                rs.getString("account_number"),
                rs.getString("sort_code"),
                rs.getString("user_id"),
                rs.getString("name"),
                rs.getString("account_type"),
                rs.getBigDecimal("balance"),
                rs.getString("currency"),
                rs.getTimestamp("created_timestamp"),
                rs.getTimestamp("updated_timestamp")
        );
    }

    public Account toDomain(AccountEntity entity) {
        return new Account(
                entity.accountNumber(),
                entity.sortCode(),
                entity.userId(),
                entity.name(),
                AccountType.fromValue(entity.accountType()),
                entity.balance(),
                entity.currency(),
                entity.createdTimestamp().toInstant(),
                entity.updatedTimestamp().toInstant()
        );
    }
}
