package com.povilas.eagle_bank.account.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CreateAccountControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void createAccount_withValidData_returns201() throws Exception {
        String userId = createUser();

        mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest(userId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(matchesPattern("^01\\d{6}$")))
                .andExpect(jsonPath("$.sortCode").value("10-10-10"))
                .andExpect(jsonPath("$.name").value("Personal Account"))
                .andExpect(jsonPath("$.accountType").value("personal"))
                .andExpect(jsonPath("$.balance").value(0.0))
                .andExpect(jsonPath("$.currency").value("GBP"))
                .andExpect(jsonPath("$.createdTimestamp").exists())
                .andExpect(jsonPath("$.updatedTimestamp").exists());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidRequests")
    void createAccount_withInvalidData_returns400WithFieldError(
            String description, String body, String expectedField) throws Exception {
        mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details[?(@.field == '%s')]".formatted(expectedField)).isNotEmpty())
                .andExpect(jsonPath("$.details[?(@.field == '%s' && @.type == 'VALIDATION_ERROR')]".formatted(expectedField)).isNotEmpty())
                .andExpect(jsonPath("$.details[?(@.field == '%s' && @.message)]".formatted(expectedField)).isNotEmpty());
    }

    static Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of("missing userId",
                        """
                        { "name": "Personal Account", "accountType": "personal" }
                        """,
                        "userId"),
                Arguments.of("blank userId",
                        """
                        { "userId": "", "name": "Personal Account", "accountType": "personal" }
                        """,
                        "userId"),
                Arguments.of("missing name",
                        """
                        { "userId": "usr-abc123", "accountType": "personal" }
                        """,
                        "name"),
                Arguments.of("blank name",
                        """
                        { "userId": "usr-abc123", "name": "", "accountType": "personal" }
                        """,
                        "name"),
                Arguments.of("missing accountType",
                        """
                        { "userId": "usr-abc123", "name": "Personal Account" }
                        """,
                        "accountType"),
                Arguments.of("blank accountType",
                        """
                        { "userId": "usr-abc123", "name": "Personal Account", "accountType": "" }
                        """,
                        "accountType")
        );
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
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private static String validRequest(String userId) {
        return """
                {
                    "userId": "%s",
                    "name": "Personal Account",
                    "accountType": "personal"
                }
                """.formatted(userId);
    }
}
