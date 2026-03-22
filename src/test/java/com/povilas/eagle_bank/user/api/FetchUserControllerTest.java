package com.povilas.eagle_bank.user.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FetchUserControllerTest extends BaseControllerTest {

    @Test
    void fetchUser_withExistingUserId_returns200() throws Exception {
        String userId = createUser();

        mockMvc.perform(get("/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.address.line1").value("123 Test Street"))
                .andExpect(jsonPath("$.address.town").value("Test Town"))
                .andExpect(jsonPath("$.address.county").value("Test County"))
                .andExpect(jsonPath("$.address.postcode").value("TE1 1ST"))
                .andExpect(jsonPath("$.phoneNumber").value("+441234567890"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.createdTimestamp").exists())
                .andExpect(jsonPath("$.updatedTimestamp").exists());
    }

    @Test
    void fetchUser_withNonExistentUserId_returns404() throws Exception {
        mockMvc.perform(get("/v1/users/{userId}", "usr-doesnotexist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

}
