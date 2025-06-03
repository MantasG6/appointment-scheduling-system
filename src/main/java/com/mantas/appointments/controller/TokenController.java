package com.mantas.appointments.controller;

import com.mantas.appointments.dto.auth.AuthResponse;
import com.mantas.appointments.dto.auth.LoginRequest;
import com.mantas.appointments.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tokens")
public class TokenController {

    private final AuthService authService;

    public TokenController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User authentication
     *
     * @param request Login request containing username and password
     * @return Token for user to use in other requests
     */
    @PostMapping
    public ResponseEntity<AuthResponse> createToken(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
