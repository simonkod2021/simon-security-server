package com.example.demo;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Triggers Flapdoodle's embedded MongoDB for all @SpringBootTest classes.
 */
@TestConfiguration
public class EmbeddedMongoTestConfig {

    @Bean
    public String embeddedMongoInit() {
        return "embedded-mongo-initialized";
    }
}