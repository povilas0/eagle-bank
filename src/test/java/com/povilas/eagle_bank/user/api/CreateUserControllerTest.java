package com.povilas.eagle_bank.user.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CreateUserControllerTest extends BaseControllerTest {

    @Test
    void createUser_withValidData_returns201() throws Exception {
        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matchesPattern("^usr-[A-Za-z0-9]+$")))
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

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidRequests")
    void createUser_withInvalidData_returns400WithFieldError(
            String description, String body, String expectedField) throws Exception {
        mockMvc.perform(post("/v1/users")
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
                        { "address": { "line1": "123 Test Street", "town": "Test Town", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "name"),
                Arguments.of("blank name",
                        """
                        { "name": "", "address": { "line1": "123 Test Street", "town": "Test Town", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "name"),
                Arguments.of("missing address",
                        """
                        { "name": "Test User", "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address"),
                Arguments.of("missing address.line1",
                        """
                        { "name": "Test User", "address": { "town": "Test Town", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address.line1"),
                Arguments.of("blank address.line1",
                        """
                        { "name": "Test User", "address": { "line1": "", "town": "Test Town", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address.line1"),
                Arguments.of("missing address.town",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address.town"),
                Arguments.of("blank address.town",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address.town"),
                Arguments.of("missing address.county",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "Test Town", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address.county"),
                Arguments.of("blank address.county",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "Test Town", "county": "", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address.county"),
                Arguments.of("missing address.postcode",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "Test Town", "county": "Test County" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address.postcode"),
                Arguments.of("blank address.postcode",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "Test Town", "county": "Test County", "postcode": "" }, "phoneNumber": "+441234567890", "email": "test@example.com" }
                        """,
                        "address.postcode"),
                Arguments.of("missing phoneNumber",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "Test Town", "county": "Test County", "postcode": "TE1 1ST" }, "email": "test@example.com" }
                        """,
                        "phoneNumber"),
                Arguments.of("blank phoneNumber",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "Test Town", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "", "email": "test@example.com" }
                        """,
                        "phoneNumber"),
                Arguments.of("missing email",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "Test Town", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890" }
                        """,
                        "email"),
                Arguments.of("blank email",
                        """
                        { "name": "Test User", "address": { "line1": "123 Test Street", "town": "Test Town", "county": "Test County", "postcode": "TE1 1ST" }, "phoneNumber": "+441234567890", "email": "" }
                        """,
                        "email")
        );
    }

    private static String validRequest() {
        return """
                {
                    "name": "Test User",
                    "address": {
                        "line1": "123 Test Street",
                        "town": "Test Town",
                        "county": "Test County",
                        "postcode": "TE1 1ST"
                    },
                    "phoneNumber": "+441234567890",
                    "email": "test@example.com",
                    "password": "Password123!"
                }
                """;
    }
}
