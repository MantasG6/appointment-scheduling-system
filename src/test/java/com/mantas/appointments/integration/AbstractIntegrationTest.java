package com.mantas.appointments.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SuppressWarnings("resource")
@SpringBootTest
@Testcontainers
public class AbstractIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("appointments")
                    .withUsername("postgres")
                    .withPassword("password");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
}