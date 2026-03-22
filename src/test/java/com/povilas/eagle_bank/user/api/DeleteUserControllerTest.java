package com.povilas.eagle_bank.user.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteUserControllerTest extends BaseControllerTest {

    @Test
    void deleteUser_withExistingUserId_returns204() throws Exception {
        String userId = createUser();

        mockMvc.perform(delete("/v1/users/{userId}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_withNonExistentUserId_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(delete("/v1/users/{userId}", "usr-doesnotexist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

}
