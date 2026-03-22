package com.povilas.eagle_bank.transaction.persistence;

import com.povilas.eagle_bank.transaction.domain.Transaction;
import com.povilas.eagle_bank.transaction.domain.TransactionType;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class TransactionPersistenceMapper {

    public TransactionEntity toEntity(Transaction transaction) {
        return new TransactionEntity(
                transaction.id(),
                transaction.accountNumber(),
                transaction.amount(),
                transaction.currency(),
                transaction.type().getValue(),
                transaction.reference(),
                Timestamp.from(transaction.createdTimestamp())
        );
    }

    public TransactionEntity toEntity(ResultSet rs) throws SQLException {
        return new TransactionEntity(
                rs.getString("id"),
                rs.getString("account_number"),
                rs.getBigDecimal("amount"),
                rs.getString("currency"),
                rs.getString("type"),
                rs.getString("reference"),
                rs.getTimestamp("created_timestamp")
        );
    }

    public Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.id(),
                entity.accountNumber(),
                entity.amount(),
                entity.currency(),
                TransactionType.fromValue(entity.type()),
                entity.reference(),
                entity.createdTimestamp().toInstant()
        );
    }
}
