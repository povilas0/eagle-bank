package com.povilas.eagle_bank.account.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DeleteAccountControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void deleteAccount_withExistingAccountNumber_returns204() throws Exception {
        String userId = createUser();
        String accountNumber = createAccount(userId);

        mockMvc.perform(delete("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/accounts/{accountNumber}", accountNumber))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAccount_withNonExistentAccountNumber_returns404WithErrorMessage() throws Exception {
        mockMvc.perform(delete("/v1/accounts/{accountNumber}", "01999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    private String createUser() throws Exception {
        String response = mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
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
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return response.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private String createAccount(String userId) throws Exception {
        String response = mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": "%s",
                                    "name": "Personal Account",
                                    "accountType": "personal"
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return response.replaceAll(".*\"accountNumber\":\"([^\"]+)\".*", "$1");
    }
}
