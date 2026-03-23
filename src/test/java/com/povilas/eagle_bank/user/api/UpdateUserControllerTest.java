package com.povilas.eagle_bank.user.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UpdateUserControllerTest extends BaseControllerTest {

    @Test
    void updateUser_withPartialData_returns200WithUpdatedAndPreservedFields() throws Exception {
        String userId = createUser("test@example.com", "Password123!");

        String updateBody = """
                {
                    "name": "Updated Name",
                    "phoneNumber": "+449876543210"
                }
                """;

        mockMvc.perform(patch("/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.phoneNumber").value("+449876543210"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.address.line1").value("123 Test Street"))
                .andExpect(jsonPath("$.address.town").value("Test Town"))
                .andExpect(jsonPath("$.address.county").value("Test County"))
                .andExpect(jsonPath("$.address.postcode").value("TE1 1ST"))
                .andExpect(jsonPath("$.createdTimestamp").exists())
                .andExpect(jsonPath("$.updatedTimestamp").exists());
    }

    @Test
    void updateUser_withNonExistentUserId_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(patch("/v1/users/{userId}", "usr-doesnotexist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

}
