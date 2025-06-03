package com.mantas.appointments.security;

import com.mantas.appointments.dto.auth.AuthResponse;
import com.mantas.appointments.dto.auth.LoginRequest;
import com.mantas.appointments.dto.auth.RegisterRequest;
import com.mantas.appointments.integration.AbstractIntegrationTest;
import com.mantas.appointments.model.User;
import com.mantas.appointments.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@Slf4j
@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTests extends AbstractIntegrationTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDB() {
        userRepository.deleteAll();
    }

    @Test
    public void registerTest() {
        RegisterRequest request = new RegisterRequest("testUsername", "testPass", "User");

        authService.register(request);
        Optional<User> newUser = userRepository.findByUsername("testUsername");

        Assertions.assertTrue(newUser.isPresent());
        Assertions.assertTrue(passwordEncoder.matches("testPass", newUser.get().getPassword()));
        Assertions.assertEquals("ROLE_USER", newUser.get().getRole());

    }

    @Test
    public void loginTest() {
        createUser();

        LoginRequest request = new LoginRequest("testUsername", "testPass");

        AuthResponse response = authService.login(request);

        Assertions.assertNotNull(response.token());
        log.info("Token received: {}", response.token());
    }

    private void createUser() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword(passwordEncoder.encode("testPass"));
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    @Test
    public void loginTestNoUser() {
        LoginRequest request = new LoginRequest("testUsername", "testPass");

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    public void loginTestWrongPassword() {
        createUser();

        LoginRequest request = new LoginRequest("testUsername", "wrongTestPass");

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    public void loginTestWrongUsername() {
        createUser();

        LoginRequest request = new LoginRequest("wrongTestUsername", "testPass");

        Assertions.assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

}
