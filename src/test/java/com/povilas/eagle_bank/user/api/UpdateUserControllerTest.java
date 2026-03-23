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
        mockMvc.perform(patch("/v1/users/{userId}", authUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated Name",
                                    "phoneNumber": "+449876543210"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authUserId))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.phoneNumber").value("+449876543210"))
                .andExpect(jsonPath("$.email").value("auth@example.com"))
                .andExpect(jsonPath("$.address.line1").value("1 Auth Street"))
                .andExpect(jsonPath("$.address.town").value("Auth Town"))
                .andExpect(jsonPath("$.address.county").value("Auth County"))
                .andExpect(jsonPath("$.address.postcode").value("AU1 1TH"))
                .andExpect(jsonPath("$.createdTimestamp").exists())
                .andExpect(jsonPath("$.updatedTimestamp").exists());
    }

    @Test
    void updateUser_withAnotherUserId_returns403() throws Exception {
        String otherUserId = createUser();

        mockMvc.perform(patch("/v1/users/{userId}", otherUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Hacked Name"}
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").exists());
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
