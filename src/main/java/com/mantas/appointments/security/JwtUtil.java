package com.mantas.appointments.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationTime}")
    private Long expirationTime;

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * Generates token for a provided user
     *
     * @param userDetails User information
     * @return Token in string format
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .claim("sub", userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority())
                .claim("iat", new Date())
                .claim("exp", new Date(System.currentTimeMillis() + expirationTime))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Extracts username from already verified JWT claims.
     * Use {@link #validateToken(String)} to receive the verified JWT claims.
     *
     * @param claims Verified claims object
     * @return Username
     */
    public String extractUsername(Claims claims) {
        return claims.get("sub", String.class);
    }

    /**
     * Validate and parse token
     *
     * @param token Token
     * @return Claims to retrieve values (username, role) from the token or empty Optional if validation failed
     */
    public Optional<Claims> validateToken(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("JWT Token validation failed: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
