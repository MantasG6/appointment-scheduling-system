package com.mantas.appointments.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUtilTests {

    private JwtUtil jwtUtil;
    private UserDetails mockUserDetails;
    private final String secret = "Lorem ipsum dolor sit amet amet.";

    private final String TOKEN_VALID = Jwts.builder()
            .claim("sub", "testUsername")
            .claim("iat", new Date())
            .claim("exp", new Date(System.currentTimeMillis() + 3600000))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
            .compact();

    private final String TOKEN_INVALID = Jwts.builder()
            .claim("sub", "testUsername")
            .claim("iat", new Date())
            .claim("exp", new Date(System.currentTimeMillis() - 1))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
            .compact();

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);

        mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("testUsername");
    }

    @Test
    void extractUsernameTest() {

        String actual = jwtUtil.extractUsername(TOKEN_VALID);
        String expected = "testUsername";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTokenTest() {

        Boolean actual = jwtUtil.validateToken(TOKEN_VALID, mockUserDetails);
        Boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTokenTestNegative() {

        Boolean actual = jwtUtil.validateToken(TOKEN_INVALID, mockUserDetails);
        Boolean expected = false;

        Assertions.assertEquals(expected, actual);
    }

}
