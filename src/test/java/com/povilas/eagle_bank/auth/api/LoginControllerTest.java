package com.povilas.eagle_bank.auth.api;

import com.povilas.eagle_bank.support.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest extends BaseControllerTest {

    @Test
    void login_withValidCredentials_returnsJwtToken() throws Exception {
        createUser("valid-login@example.com", "Password123!");

        mockMvcNoAuth.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "valid-login@example.com",
                                    "password": "Password123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_withInvalidPassword_returns401WithErrorMessage() throws Exception {
        createUser("wrong-pass@example.com", "Password123!");

        mockMvcNoAuth.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "wrong-pass@example.com",
                                    "password": "WrongPassword"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void login_withNonExistentEmail_returns401WithErrorMessage() throws Exception {
        mockMvcNoAuth.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "notfound@example.com",
                                    "password": "Password123!"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void login_withMissingFields_returns400WithValidationErrors() throws Exception {
        mockMvcNoAuth.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void protectedEndpoint_withoutToken_returns401() throws Exception {
        String userId = createUser();

        mockMvcNoAuth.perform(get("/v1/users/{id}", userId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_withInvalidToken_returns401() throws Exception {
        String userId = createUser();

        mockMvcNoAuth.perform(get("/v1/users/{id}", userId)
                        .header("Authorization", "Bearer invalid.jwt.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_withValidToken_returns200() throws Exception {
        String userId = createUser("auth-test@example.com", "Password123!");

        var loginResult = mockMvcNoAuth.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "auth-test@example.com",
                                    "password": "Password123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();

        mockMvcNoAuth.perform(get("/v1/users/{id}", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
