package com.povilas.eagle_bank.account.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateAccountControllerTest extends BaseControllerTest {

    @Test
    void createAccount_withValidData_returns201() throws Exception {
        mockMvc.perform(post("/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Personal Account",
                                    "accountType": "personal"
                                }
                                """))
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
                Arguments.of("missing name",
                        """
                        { "accountType": "personal" }
                        """,
                        "name"),
                Arguments.of("blank name",
                        """
                        { "name": "", "accountType": "personal" }
                        """,
                        "name"),
                Arguments.of("missing accountType",
                        """
                        { "name": "Personal Account" }
                        """,
                        "accountType"),
                Arguments.of("blank accountType",
                        """
                        { "name": "Personal Account", "accountType": "" }
                        """,
                        "accountType")
        );
    }
}
