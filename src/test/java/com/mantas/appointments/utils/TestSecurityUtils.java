package com.mantas.appointments.utils;

import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.List;

import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_OWNER_ID;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

/**
 * Utility class for setting up security in tests.
 */
public class TestSecurityUtils {

    private TestSecurityUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a {@link SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor} with the specified role.
     *
     * @param role the role to be added to the JWT
     * @return a {@link SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor} with the specified role
     */
    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtWithRole(String role) {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }

    /**
     * Initializes the security context with a default test user authentication.
     * The default user has a predefined owner ID.
     */
    public static void initializeDefaultTestUserAuthentication() {
        Jwt jwt = Mockito.mock(Jwt.class);
        Mockito.when(jwt.getSubject()).thenReturn(DEFAULT_OWNER_ID);
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
