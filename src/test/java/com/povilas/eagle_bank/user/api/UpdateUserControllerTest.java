package com.povilas.eagle_bank.user.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UpdateUserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void updateUser_withPartialData_returns200WithUpdatedAndPreservedFields() throws Exception {
        String userId = createUser();

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

    private String createUser() throws Exception {
        String createBody = """
                {
                    "name": "Test User",
                    "address": {
                        "line1": "123 Test Street",
                        "town": "Test Town",
                        "county": "Test County",
                        "postcode": "TE1 1ST"
                    },
                    "phoneNumber": "+441234567890",
                    "email": "test@example.com"
                }
                """;

        String response = mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }
}
