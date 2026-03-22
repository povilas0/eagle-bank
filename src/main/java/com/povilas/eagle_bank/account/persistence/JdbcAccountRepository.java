package com.povilas.eagle_bank.account.persistence;

import com.povilas.eagle_bank.account.domain.Account;
import com.povilas.eagle_bank.account.domain.AccountRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAccountRepository implements AccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AccountPersistenceMapper mapper;

    public JdbcAccountRepository(NamedParameterJdbcTemplate jdbcTemplate, AccountPersistenceMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        var params = new MapSqlParameterSource("accountNumber", accountNumber);
        return jdbcTemplate.query(
                """
                SELECT account_number, sort_code, user_id, name, account_type,
                    balance, currency, created_timestamp, updated_timestamp
                FROM accounts WHERE account_number = :accountNumber
                """,
                params,
                (rs, rowNum) -> mapper.toDomain(mapper.toEntity(rs))
        ).stream().findFirst();
    }

    @Override
    public List<Account> findByUserId(String userId) {
        var params = new MapSqlParameterSource("userId", userId);
        return jdbcTemplate.query(
                """
                SELECT account_number, sort_code, user_id, name, account_type,
                    balance, currency, created_timestamp, updated_timestamp
                FROM accounts WHERE user_id = :userId
                """,
                params,
                (rs, rowNum) -> mapper.toDomain(mapper.toEntity(rs))
        );
    }

    @Override
    public void update(Account account) {
        AccountEntity entity = mapper.toEntity(account);
        var params = new MapSqlParameterSource()
                .addValue("accountNumber", entity.accountNumber())
                .addValue("name", entity.name())
                .addValue("accountType", entity.accountType())
                .addValue("updatedTimestamp", entity.updatedTimestamp());
        jdbcTemplate.update(
                """
                UPDATE accounts
                SET name = :name, account_type = :accountType, updated_timestamp = :updatedTimestamp
                WHERE account_number = :accountNumber
                """,
                params
        );
    }

    @Override
    public void save(Account account) {
        AccountEntity entity = mapper.toEntity(account);
        var params = new MapSqlParameterSource()
                .addValue("accountNumber", entity.accountNumber())
                .addValue("sortCode", entity.sortCode())
                .addValue("userId", entity.userId())
                .addValue("name", entity.name())
                .addValue("accountType", entity.accountType())
                .addValue("balance", entity.balance())
                .addValue("currency", entity.currency())
                .addValue("createdTimestamp", entity.createdTimestamp())
                .addValue("updatedTimestamp", entity.updatedTimestamp());
        jdbcTemplate.update(
                """
                INSERT INTO accounts (account_number, sort_code, user_id, name, account_type,
                    balance, currency, created_timestamp, updated_timestamp)
                VALUES (:accountNumber, :sortCode, :userId, :name, :accountType,
                    :balance, :currency, :createdTimestamp, :updatedTimestamp)
                """,
                params
        );
    }
}
