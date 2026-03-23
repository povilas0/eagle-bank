package com.povilas.eagle_bank.account.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteAccountControllerTest extends BaseControllerTest {

    @Test
    void deleteAccount_withOwnAccountNumber_returns204() throws Exception {
        String accountNumber = createAccount(authUserId);

        mockMvc.perform(delete("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_withAnotherUsersAccount_returns403() throws Exception {
        String otherUserId = createUser();
        String accountNumber = createAccount(otherUserId);

        mockMvc.perform(delete("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deleteAccount_withNonExistentAccountNumber_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(delete("/v1/accounts/{accountNumber}", "01999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
