package com.povilas.eagle_bank.transaction.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListTransactionsControllerTest extends BaseControllerTest {

    @Test
    void listTransactions_returnsAllTransactionsForAccount() throws Exception {
        String userId = createUser();
        String accountNumber = createAccount(userId);
        createTransaction(accountNumber, 100.00, "deposit", "First deposit");
        createTransaction(accountNumber, 50.00, "deposit", "Second deposit");

        mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(2))
                .andExpect(jsonPath("$.transactions[0].amount").value(100.00))
                .andExpect(jsonPath("$.transactions[0].type").value("deposit"))
                .andExpect(jsonPath("$.transactions[0].reference").value("First deposit"))
                .andExpect(jsonPath("$.transactions[1].amount").value(50.00))
                .andExpect(jsonPath("$.transactions[1].type").value("deposit"))
                .andExpect(jsonPath("$.transactions[1].reference").value("Second deposit"));
    }

    @Test
    void listTransactions_returnsEmptyListWhenAccountHasNoTransactions() throws Exception {
        String userId = createUser();
        String accountNumber = createAccount(userId);

        mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(0));
    }

    @Test
    void listTransactions_withNonExistentAccountNumber_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions", "01999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

}
