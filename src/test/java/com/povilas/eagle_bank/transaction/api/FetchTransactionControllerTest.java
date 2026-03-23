package com.povilas.eagle_bank.transaction.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FetchTransactionControllerTest extends BaseControllerTest {

    @Test
    void fetchTransaction_returnsTransaction() throws Exception {
        String accountNumber = createAccount(authUserId);
        String transactionId = createTransaction(accountNumber, 75.00, "deposit", "Test deposit");

        mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions/{transactionId}", accountNumber, transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transactionId))
                .andExpect(jsonPath("$.amount").value(75.00))
                .andExpect(jsonPath("$.currency").value("GBP"))
                .andExpect(jsonPath("$.type").value("deposit"))
                .andExpect(jsonPath("$.reference").value("Test deposit"));
    }

    @Test
    void fetchTransaction_withAnotherUsersAccount_returns403() throws Exception {
        String otherUserId = createUser();
        String accountNumber = createAccount(otherUserId);

        mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions/{transactionId}", accountNumber, "tan-abc"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void fetchTransaction_withNonExistentAccountNumber_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions/{transactionId}", "01999999", "tan-abc"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void fetchTransaction_withTransactionFromWrongAccount_returns404WithErrorMessage() throws Exception {
        String accountNumber1 = createAccount(authUserId);
        String accountNumber2 = createAccount(authUserId);
        String transactionId = createTransaction(accountNumber2, 50.00, "deposit", "Test deposit");

        mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions/{transactionId}", accountNumber1, transactionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void fetchTransaction_withNonExistentTransactionId_returns404WithErrorMessage() throws Exception {
        String accountNumber = createAccount(authUserId);

        mockMvc.perform(get("/v1/accounts/{accountNumber}/transactions/{transactionId}", accountNumber, "tan-nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

}
