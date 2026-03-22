package com.povilas.eagle_bank.transaction.persistence;

import com.povilas.eagle_bank.transaction.domain.Transaction;
import com.povilas.eagle_bank.transaction.domain.TransactionRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTransactionRepository implements TransactionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TransactionPersistenceMapper mapper;

    public JdbcTransactionRepository(NamedParameterJdbcTemplate jdbcTemplate, TransactionPersistenceMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
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
