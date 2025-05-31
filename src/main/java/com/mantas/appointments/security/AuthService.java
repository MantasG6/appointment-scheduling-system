package com.mantas.appointments.security;

import com.mantas.appointments.dto.auth.AuthResponse;
import com.mantas.appointments.dto.auth.LoginRequest;
import com.mantas.appointments.dto.auth.RegisterRequest;
import com.mantas.appointments.model.User;
import com.mantas.appointments.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authManager, JwtUtil jwtUtil,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Creates new user
     *
     * @param request Registration request containing user information
     */
    public void register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_" + request.role().toUpperCase());

        userRepository.save(user);
    }

    /**
     * Authenticate existing user
     *
     * @param request Login request containing username and password
     * @return Token to use for following user requests
     */
    public AuthResponse login(LoginRequest request) {
        Authentication auth = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        authManager.authenticate(auth);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String token = jwtUtil.generateToken(userDetails);
        return new AuthResponse(token);
    }
}
