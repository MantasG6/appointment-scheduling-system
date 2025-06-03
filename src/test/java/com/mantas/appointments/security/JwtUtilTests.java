package com.mantas.appointments.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnnecessaryLocalVariable")
public class JwtUtilTests {

    private JwtUtil jwtUtil;
    private final String secret = "Lorem ipsum dolor sit amet amet.";
    private final String TEST_USERNAME = "testUsername";

    private final String TOKEN_VALID = Jwts.builder()
            .claim("sub", TEST_USERNAME)
            .claim("iat", new Date())
            .claim("exp", new Date(System.currentTimeMillis() + 3600000))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
            .compact();

    private final String TOKEN_INVALID = Jwts.builder()
            .claim("sub", TEST_USERNAME)
            .claim("iat", new Date())
            .claim("exp", new Date(System.currentTimeMillis() - 1))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
            .compact();

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
    }

    @Test
    void extractUsernameTest() {

        Claims claims = mock(Claims.class);
        when(claims.get("sub", String.class)).thenReturn(TEST_USERNAME);

        String actual = jwtUtil.extractUsername(claims);
        String expected = TEST_USERNAME;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTokenTest() {

        Optional<Claims> validatedClaims = jwtUtil.validateToken(TOKEN_VALID);

        Assertions.assertTrue(validatedClaims.isPresent());

        String actual = validatedClaims.get().get("sub", String.class);
        String expected = TEST_USERNAME;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTokenTestExpired() {

        Optional<Claims> validatedClaims = jwtUtil.validateToken(TOKEN_INVALID);

        Assertions.assertTrue(validatedClaims.isEmpty());
    }

}
