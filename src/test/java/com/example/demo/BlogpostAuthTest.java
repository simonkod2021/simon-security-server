package com.example.demo;

import com.example.demo.models.Roles;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogpostAuthTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${jwt.secret}")
    private String SECRET;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setId("poster"); // ← Set the ID to "poster"
        user.setUsername("posterUser");
        user.setPassword("$2a$10$dXJ3SW6G7P.XBLBvanJY.9ZP3l.0AnlDdLfuK6yv7.ZsVtJXoYzW2");
        user.setRoles(Set.of(Roles.USER));

        userRepository.save(user);

        // Verify
        Optional<User> found = userRepository.findById("poster");
        System.out.println("User found with ID 'poster': " + found.isPresent());
    }


    @Test
    void cannotCreateBlogpostWithoutToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> req = new HttpEntity<>(Map.of(
                "title", "Test Title",
                "content", "Test content with enough length"
        ), headers);

        ResponseEntity<String> resp = restTemplate.postForEntity(
                "/api/auth/blogposts/create",
                req,
                String.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    void canCreateBlogpostWithJwt() {
        String jwt = Jwts.builder()
                .subject("poster")
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> req = new HttpEntity<>(Map.of(
                "title", "Valid Title for testing",
                "content", "This is valid content for testing the code"
        ), headers);

        ResponseEntity<String> resp = restTemplate.postForEntity(
                "/api/auth/blogposts/create",
                req,
                String.class
        );

        // Debug the actual response
        System.out.println("Response Status: " + resp.getStatusCode());
        System.out.println("Response Body: " + resp.getBody());
        System.out.println("Response Headers: " + resp.getHeaders());

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
