package com.povilas.eagle_bank.transaction.domain;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @Test
    void createTransaction_hasTransactionalAnnotation() throws NoSuchMethodException {
        Method method = TransactionService.class.getDeclaredMethod("createTransaction", CreateTransactionCommand.class);
        Transactional transactional = method.getAnnotation(Transactional.class);

        assertNotNull(transactional);
        assertFalse(transactional.readOnly());
    }

    @Test
    void getTransaction_hasReadOnlyTransactionalAnnotation() throws NoSuchMethodException {
        Method method = TransactionService.class.getDeclaredMethod("getTransaction", String.class, String.class, String.class);
        Transactional transactional = method.getAnnotation(Transactional.class);

        assertNotNull(transactional);
        assertTrue(transactional.readOnly());
    }

    @Test
    void listTransactions_hasReadOnlyTransactionalAnnotation() throws NoSuchMethodException {
        Method method = TransactionService.class.getDeclaredMethod("listTransactions", String.class, String.class);
        Transactional transactional = method.getAnnotation(Transactional.class);

        assertNotNull(transactional);
        assertTrue(transactional.readOnly());
    }
}
