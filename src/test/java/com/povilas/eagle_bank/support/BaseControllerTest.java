package com.povilas.eagle_bank.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public abstract class BaseControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected MockMvc mockMvc;
    protected MockMvc mockMvcNoAuth;
    protected String authUserId;

    private final Map<String, String> userEmails = new HashMap<>();

    @BeforeEach
    void setUp() throws Exception {
        jdbcTemplate.execute("DELETE FROM transactions");
        jdbcTemplate.execute("DELETE FROM accounts");
        jdbcTemplate.execute("DELETE FROM users");
        userEmails.clear();

        mockMvcNoAuth = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();

        authUserId = createAuthUser();
        String token = getToken("auth@example.com");

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .defaultRequest(get("/").header("Authorization", "Bearer " + token))
                .build();
    }

    private String createAuthUser() throws Exception {
        var result = mockMvcNoAuth.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Auth Test User",
                                    "address": {
                                        "line1": "1 Auth Street",
                                        "town": "Auth Town",
                                        "county": "Auth County",
                                        "postcode": "AU1 1TH"
                                    },
                                    "phoneNumber": "+441234567890",
                                    "email": "auth@example.com",
                                    "password": "Password123!"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    private String getToken(String email) throws Exception {
        var result = mockMvcNoAuth.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "%s",
                                    "password": "Password123!"
                                }
                                """.formatted(email)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    private MockMvc buildMockMvcFor(String userId) throws Exception {
        String email = userEmails.get(userId);
        String token = getToken(email);
        return MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .defaultRequest(get("/").header("Authorization", "Bearer " + token))
                .build();
    }

    protected String createUser() throws Exception {
        return createUser("user-" + UUID.randomUUID() + "@example.com", "Password123!");
    }

    protected String createUser(String email, String password) throws Exception {
        var result = mockMvcNoAuth.perform(post("/v1/users")
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
                                    "email": "%s",
                                    "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().isCreated())
                .andReturn();

        String userId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
        userEmails.put(userId, email);
        return userId;
    }

    protected String createAccount(String userId) throws Exception {
        return createAccount(userId, "Personal Account");
    }

    protected String createAccount(String userId, String name) throws Exception {
        MockMvc mc = userId.equals(authUserId) ? mockMvc : buildMockMvcFor(userId);
        var result = mc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "%s",
                                    "accountType": "personal"
                                }
                                """.formatted(name)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("accountNumber").asText();
    }

    protected String createTransaction(String accountNumber, double amount, String type, String reference) throws Exception {
        var result = mockMvc.perform(post("/v1/accounts/{accountNumber}/transactions", accountNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": %s,
                                    "currency": "GBP",
                                    "type": "%s",
                                    "reference": "%s"
                                }
                                """.formatted(amount, type, reference)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }
}
