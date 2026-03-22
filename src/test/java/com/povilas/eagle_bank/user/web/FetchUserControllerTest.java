package com.povilas.eagle_bank.user.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class FetchUserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

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

    private String createUser() throws Exception {
        String body = """
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
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }
}
