package com.povilas.eagle_bank.account.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FetchAccountControllerTest extends BaseControllerTest {

    @Test
    void fetchAccount_withOwnAccountNumber_returns200() throws Exception {
        String accountNumber = createAccount(authUserId);

        mockMvc.perform(get("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.sortCode").value("10-10-10"))
                .andExpect(jsonPath("$.name").value("Personal Account"))
                .andExpect(jsonPath("$.accountType").value("personal"))
                .andExpect(jsonPath("$.balance").value(0.0))
                .andExpect(jsonPath("$.currency").value("GBP"))
                .andExpect(jsonPath("$.createdTimestamp").exists())
                .andExpect(jsonPath("$.updatedTimestamp").exists());
    }

    @Test
    void fetchAccount_withAnotherUsersAccountNumber_returns403() throws Exception {
        String otherUserId = createUser();
        String accountNumber = createAccount(otherUserId);

        mockMvc.perform(get("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void fetchAccount_withNonExistentAccountNumber_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(get("/v1/accounts/{accountNumber}", "01999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
