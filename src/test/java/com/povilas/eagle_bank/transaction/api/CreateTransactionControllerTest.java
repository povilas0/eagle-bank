package com.povilas.eagle_bank.transaction.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateTransactionControllerTest extends BaseControllerTest {

    @Test
    void createTransaction_deposit_returns201WithTransactionResponse() throws Exception {
        String userId = createUser();
        String accountNumber = createAccount(userId);

        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 100.00,
                                    "currency": "GBP",
                                    "type": "deposit",
                                    "reference": "Initial deposit"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.currency").value("GBP"))
                .andExpect(jsonPath("$.type").value("deposit"))
                .andExpect(jsonPath("$.reference").value("Initial deposit"))
                .andExpect(jsonPath("$.createdTimestamp").exists());
    }

    @Test
    void createTransaction_deposit_updatesAccountBalance() throws Exception {
        String accountNumber = createAccount(authUserId);

        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 250.00,
                                    "currency": "GBP",
                                    "type": "deposit"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(250.00));
    }

    @Test
    void createTransaction_withdrawalWithSufficientFunds_returns201WithTransactionResponse() throws Exception {
        String userId = createUser();
        String accountNumber = createAccount(userId);
        deposit(accountNumber, 500.00);

        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 200.00,
                                    "currency": "GBP",
                                    "type": "withdrawal"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(200.00))
                .andExpect(jsonPath("$.currency").value("GBP"))
                .andExpect(jsonPath("$.type").value("withdrawal"))
                .andExpect(jsonPath("$.createdTimestamp").exists());
    }

    @Test
    void createTransaction_withdrawalWithSufficientFunds_updatesAccountBalance() throws Exception {
        String accountNumber = createAccount(authUserId);
        deposit(accountNumber, 500.00);

        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 200.00,
                                    "currency": "GBP",
                                    "type": "withdrawal"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(300.00));
    }

    @Test
    void createTransaction_withdrawalWithInsufficientFunds_returns422WithErrorMessage() throws Exception {
        String userId = createUser();
        String accountNumber = createAccount(userId);

        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 100.00,
                                    "currency": "GBP",
                                    "type": "withdrawal"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createTransaction_withNonExistentAccountNumber_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", "01999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 100.00,
                                    "currency": "GBP",
                                    "type": "deposit"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createTransaction_withMissingRequiredFields_returns400WithValidationErrors() throws Exception {
        String userId = createUser();
        String accountNumber = createAccount(userId);

        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray());
    }

    private void deposit(String accountNumber, double amount) throws Exception {
        mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": %s,
                                    "currency": "GBP",
                                    "type": "deposit"
                                }
                                """.formatted(amount)))
                .andExpect(status().isCreated());
    }
}
