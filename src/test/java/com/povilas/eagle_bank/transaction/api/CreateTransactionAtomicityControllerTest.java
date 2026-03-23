package com.povilas.eagle_bank.transaction.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import com.povilas.eagle_bank.transaction.domain.Transaction;
import com.povilas.eagle_bank.transaction.persistence.JdbcTransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateTransactionAtomicityControllerTest extends BaseControllerTest {

    @MockitoSpyBean
    private JdbcTransactionRepository transactionRepository;

    @Test
    void createTransaction_whenPersistenceFails_rollsBackBalanceUpdate() throws Exception {
        String accountNumber = createAccount(authUserId);

        doThrow(new RuntimeException("Simulated DB failure")).when(transactionRepository).save(any(Transaction.class));

        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 100.00,
                                    "currency": "GBP",
                                    "type": "deposit"
                                }
                                """))
                .andExpect(status().is5xxServerError());

        mockMvc.perform(get("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0.00));
    }
}
