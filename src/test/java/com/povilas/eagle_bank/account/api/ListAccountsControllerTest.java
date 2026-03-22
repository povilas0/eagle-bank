package com.povilas.eagle_bank.account.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ListAccountsControllerTest extends BaseControllerTest {

    @Test
    void listAccounts_returnsAllAccountsForUser() throws Exception {
        String userId = createUser();
        createAccount(userId, "Current Account");
        createAccount(userId, "Savings Account");

        mockMvc.perform(get("/v1/accounts").param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts").isArray())
                .andExpect(jsonPath("$.accounts.length()").value(2))
                .andExpect(jsonPath("$.accounts[0].name").value("Current Account"))
                .andExpect(jsonPath("$.accounts[1].name").value("Savings Account"));
    }

    @Test
    void listAccounts_returnsEmptyListWhenUserHasNoAccounts() throws Exception {
        String userId = createUser();

        mockMvc.perform(get("/v1/accounts").param("userId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts").isArray())
                .andExpect(jsonPath("$.accounts.length()").value(0));
    }

}
