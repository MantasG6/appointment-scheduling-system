package com.mantas.appointments.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Security configuration for the application.
 * This class configures the security settings, including JWT authentication and role-based access control.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Converts JWT claims to authorities based on realm roles.
     * This method extracts roles from the "realm_access" claim and converts them to SimpleGrantedAuthority.
     *
     * @return AuthoritiesConverter that converts JWT claims to a list of granted authorities.
     */
    @Bean
    AuthoritiesConverter realmRolesAuthoritiesConverter() {
        return claims -> {
            Object realmAccessObj = claims.get("realm_access");
            if (!(realmAccessObj instanceof Map<?, ?> realmAccess)) {
                return List.of();
            }

            Object rolesObj = realmAccess.get("roles");
            if (!(rolesObj instanceof List<?> rolesList)) {
                return List.of();
            }

            return rolesList.stream()
                    .filter(String.class::isInstance)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toUnmodifiableList());
        };
    }

    /**
     * Configures the JwtAuthenticationConverter to use the realmRolesAuthoritiesConverter.
     * This converter will extract roles from the JWT claims and convert them to authorities.
     *
     * @return JwtAuthenticationConverter configured with the realmRolesAuthoritiesConverter.
     */
    @Bean
    JwtAuthenticationConverter authenticationConverter() {
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> realmRolesAuthoritiesConverter().convert(jwt.getClaims()));
        return authenticationConverter;
    }

    /**
     * Configures the security filter chain for the application.
     * This method sets up JWT resource server support, session management, CSRF protection, and authorization rules.
     *
     * @param http HttpSecurity object to configure security settings.
     * @return SecurityFilterChain configured with the specified security settings.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(jwtDecoder ->
                                jwtDecoder.jwtAuthenticationConverter(authenticationConverter())))
                .sessionManagement(sessions ->
                        sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/api/v1/services/**").hasRole("PROVIDER");
                    requests.anyRequest().authenticated();
                })
                .build();
    }

    /**
     * Provides an AuthenticationManager bean for authentication configuration.
     * This bean is used to manage authentication processes in the application.
     *
     * @param config AuthenticationConfiguration object to retrieve the AuthenticationManager.
     * @return AuthenticationManager configured for the application.
     * @throws Exception if an error occurs while retrieving the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a PasswordEncoder bean for encoding passwords.
     * This bean uses BCrypt hashing algorithm to encode passwords securely.
     *
     * @return PasswordEncoder configured with BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
