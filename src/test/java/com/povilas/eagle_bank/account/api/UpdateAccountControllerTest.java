package com.povilas.eagle_bank.account.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateAccountControllerTest extends BaseControllerTest {

    @Test
    void updateAccount_withOwnAccount_returns200WithUpdatedAccount() throws Exception {
        String accountNumber = createAccount(authUserId);

        mockMvc.perform(patch("/v1/accounts/{accountNumber}", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated Account Name"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(accountNumber))
                .andExpect(jsonPath("$.name").value("Updated Account Name"))
                .andExpect(jsonPath("$.accountType").value("personal"))
                .andExpect(jsonPath("$.sortCode").value("10-10-10"))
                .andExpect(jsonPath("$.currency").value("GBP"))
                .andExpect(jsonPath("$.createdTimestamp").exists())
                .andExpect(jsonPath("$.updatedTimestamp").exists());
    }

    @Test
    void updateAccount_withAnotherUsersAccount_returns403() throws Exception {
        String otherUserId = createUser();
        String accountNumber = createAccount(otherUserId);

        mockMvc.perform(patch("/v1/accounts/{accountNumber}", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Hacked Name"
                                }
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void updateAccount_withNonExistentAccountNumber_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(patch("/v1/accounts/{accountNumber}", "01999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated Account Name"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
