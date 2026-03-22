package com.povilas.eagle_bank.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public abstract class BaseControllerTest {

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    protected String createUser() throws Exception {
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

    protected String createAccount(String userId) throws Exception {
        return createAccount(userId, "Personal Account");
    }

    protected String createAccount(String userId, String name) throws Exception {
        String response = mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userId": "%s",
                                    "name": "%s",
                                    "accountType": "personal"
                                }
                                """.formatted(userId, name)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return response.replaceAll(".*\"accountNumber\":\"([^\"]+)\".*", "$1");
    }
}
