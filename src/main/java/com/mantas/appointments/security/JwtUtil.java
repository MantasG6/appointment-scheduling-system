package com.mantas.appointments.security;


import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationTime}")
    private Long expirationTime;

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
     * Extracts username from a token
     *
     * @param token Token in string format
     * @return Username
     */
    public String extractUsername(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build();

        return parser.parseSignedClaims(token).getPayload().get("sub", String.class);
    }

    /**
     * Validates token
     *
     * @param token       Token
     * @param userDetails User information
     * @return True if token is valid
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
    }

    /**
     * Checks if token is expired
     *
     * @param token Token
     * @return True if token is expired
     */
    private boolean isTokenExpired(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build();

        Date expiration = parser.parseSignedClaims(token)
                .getPayload()
                .get("exp", Date.class);

        return expiration.before(new Date());
    }

}
