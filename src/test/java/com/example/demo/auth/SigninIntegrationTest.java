package com.example.demo.auth;

import com.example.demo.BaseIntegrationTest;
import com.example.demo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SigninIntegrationTest extends BaseIntegrationTest {

    @Autowired
    TestRestTemplate rest;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanAndSetupUser() {
        // Remove existing test user to avoid duplicate errors
        mongoTemplate.remove(Query.query(Criteria.where("username").is("loginUser")), User.class);

        // Register a user
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> signupRequest = new HttpEntity<>(Map.of(
                "username", "loginUser",
                "password", "Pass123"
        ), headers);

        rest.postForEntity("/api/auth/signup", signupRequest, String.class);
    }

    @Test
    void signinReturnsJwtToken() {
        // Login request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of(
                "username", "loginUser",
                "password", "Pass123"
        ), headers);

        ResponseEntity<Map> response = rest.postForEntity("/api/auth/signin", request, Map.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull().containsKey("jwtToken");

    }

    @Test
    void signinFailsForWrongPassword() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(Map.of(
                "username", "loginUser",
                "password", "wrong"
        ), headers);

        ResponseEntity<String> resp = rest.postForEntity("/api/auth/signin", request, String.class);

        assertThat(resp.getStatusCode().value()).isEqualTo(401);
    }
}
