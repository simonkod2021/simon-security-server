package com.example.demo.auth;

import com.example.demo.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SignupIntegrationTest extends BaseIntegrationTest {

    @Autowired
    TestRestTemplate rest;

    @Test
    void signupCreatesUserSuccessfully() {
        Map<String, String> body = Map.of(
                "username", "testuser",
                "password", "Password123"
        );

        ResponseEntity<String> resp =
                rest.postForEntity("/api/auth/signup", body, String.class);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void signupValidationFailsForEmptyPassword() {
        Map<String, String> body = Map.of(
                "username", "newuser",
                "password", ""
        );

        ResponseEntity<String> resp =
                rest.postForEntity("/api/auth/signup", body, String.class);

        assertThat(resp.getStatusCode().is4xxClientError()).isTrue();
    }
}