package com.povilas.eagle_bank.transaction.persistence;

import com.povilas.eagle_bank.transaction.domain.Transaction;
import com.povilas.eagle_bank.transaction.domain.TransactionRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTransactionRepository implements TransactionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TransactionPersistenceMapper mapper;

    public JdbcTransactionRepository(NamedParameterJdbcTemplate jdbcTemplate, TransactionPersistenceMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Optional<Transaction> findById(String transactionId) {
        var params = new MapSqlParameterSource("id", transactionId);
        return jdbcTemplate.query(
                """
                SELECT id, account_number, amount, currency, type, reference, created_timestamp
                FROM transactions WHERE id = :id
                """,
                params,
                (rs, rowNum) -> mapper.toDomain(mapper.toEntity(rs))
        ).stream().findFirst();
    }

    @Override
    public List<Transaction> findByAccountNumber(String accountNumber) {
        var params = new MapSqlParameterSource("accountNumber", accountNumber);
        return jdbcTemplate.query(
                """
                SELECT id, account_number, amount, currency, type, reference, created_timestamp
                FROM transactions WHERE account_number = :accountNumber
                """,
                params,
                (rs, rowNum) -> mapper.toDomain(mapper.toEntity(rs))
        );
    }

    @Override
    public void save(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);
        var params = new MapSqlParameterSource()
                .addValue("id", entity.id())
                .addValue("accountNumber", entity.accountNumber())
                .addValue("amount", entity.amount())
                .addValue("currency", entity.currency())
                .addValue("type", entity.type())
                .addValue("reference", entity.reference())
                .addValue("createdTimestamp", entity.createdTimestamp());
        jdbcTemplate.update(
                """
                INSERT INTO transactions (id, account_number, amount, currency, type, reference, created_timestamp)
                VALUES (:id, :accountNumber, :amount, :currency, :type, :reference, :createdTimestamp)
                """,
                params
        );
    }
}
