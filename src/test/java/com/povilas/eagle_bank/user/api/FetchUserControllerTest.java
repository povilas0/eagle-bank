package com.povilas.eagle_bank.user.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FetchUserControllerTest extends BaseControllerTest {

    @Test
    void fetchUser_returnsOwnProfile() throws Exception {
        mockMvc.perform(get("/v1/users/{userId}", authUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authUserId))
                .andExpect(jsonPath("$.name").value("Auth Test User"))
                .andExpect(jsonPath("$.address.line1").value("1 Auth Street"))
                .andExpect(jsonPath("$.address.town").value("Auth Town"))
                .andExpect(jsonPath("$.address.county").value("Auth County"))
                .andExpect(jsonPath("$.address.postcode").value("AU1 1TH"))
                .andExpect(jsonPath("$.phoneNumber").value("+441234567890"))
                .andExpect(jsonPath("$.email").value("auth@example.com"))
                .andExpect(jsonPath("$.createdTimestamp").exists())
                .andExpect(jsonPath("$.updatedTimestamp").exists());
    }

    @Test
    void fetchUser_withAnotherUserId_returns403() throws Exception {
        String otherUserId = createUser();

        mockMvc.perform(get("/v1/users/{userId}", otherUserId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void fetchUser_withNonExistentUserId_returns404() throws Exception {
        mockMvc.perform(get("/v1/users/{userId}", "usr-doesnotexist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
