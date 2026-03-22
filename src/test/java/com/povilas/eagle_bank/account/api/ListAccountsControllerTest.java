package com.povilas.eagle_bank.account.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ListAccountsControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

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

    private void createAccount(String userId, String name) throws Exception {
        mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": "%s",
                                    "name": "%s",
                                    "accountType": "personal"
                                }
                                """.formatted(userId, name)))
                .andExpect(status().isCreated());
    }
}
