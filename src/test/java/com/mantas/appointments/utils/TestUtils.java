package com.mantas.appointments.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


public final class TestUtils {

    // Private constructor to prevent instantiation
    private TestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static JwtRequestPostProcessor jwtWithRole(String role) {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }

    public static String entityNotFoundMessage(Long id) {
        return "Entity not found with id: " + id;
    }
}
