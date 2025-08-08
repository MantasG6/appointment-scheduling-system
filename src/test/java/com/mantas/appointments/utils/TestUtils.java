package com.mantas.appointments.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

/**
 * Utility class for test-related methods.
 */
public final class TestUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private TestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a {@link JwtRequestPostProcessor} with the specified role.
     *
     * @param role the role to be added to the JWT
     * @return a {@link JwtRequestPostProcessor} with the specified role
     */
    public static JwtRequestPostProcessor jwtWithRole(String role) {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }

    /**
     * Generates an error message for when an entity is not found by its ID.
     *
     * @param id the ID of the entity that was not found
     * @return a formatted error message
     */
    public static String entityNotFoundMessage(Long id) {
        return "Entity not found with id: " + id;
    }
}
