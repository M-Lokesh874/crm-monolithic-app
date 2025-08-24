package com.crm.controller;

import com.crm.dto.AuthRequestDTO;
import com.crm.dto.AuthResponseDTO;
import com.crm.dto.UserRegistrationDTO;
import com.crm.service.AuthService;
import com.crm.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication operations.
 * Provides endpoints for user login, token validation, and health checks.
 * These endpoints are publicly accessible and do not require authentication.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Registers a new user in the system.
     * This endpoint is publicly accessible and allows new users to create accounts.
     * 
     * @param registrationRequest The user registration request
     * @return ResponseEntity containing authentication response with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationRequest) {
        try {
            AuthResponseDTO response = authService.registerUser(registrationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Authenticates a user with the provided credentials.
     * Public endpoint - no authentication required.
     * 
     * @param authRequest The authentication request containing username and password
     * @return ResponseEntity containing authentication response with JWT token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequest) {
        try {
            AuthResponseDTO response = authService.authenticateUser(authRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Validates a JWT token.
     * Public endpoint - no authentication required.
     * 
     * @param token The JWT token to validate
     * @return ResponseEntity indicating if the token is valid
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        try {
            authService.validateToken(token);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    
    /**
     * Gets the current authenticated user's information.
     * Requires valid JWT token in Authorization header.
     * 
     * @return ResponseEntity containing current user information
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                return userService.getUserByUsername(username)
                        .map(user -> ResponseEntity.ok(user))
                        .orElse(ResponseEntity.status(401).build());
            }
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
    
    /**
     * Health check endpoint for the authentication service.
     * Public endpoint - no authentication required.
     * 
     * @return ResponseEntity indicating the service is healthy
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is healthy");
    }

    /**
     * Logs out the current user by invalidating their session.
     * In a JWT-based system, this endpoint can be used to track logout events
     * or add tokens to a blacklist if needed.
     * 
     * @return ResponseEntity indicating successful logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // In a JWT-based system, the actual logout happens on the client side
        // by removing the token. This endpoint can be used for audit logging
        // or to invalidate tokens if implementing a blacklist.
        return ResponseEntity.ok().body(Map.of("message", "Logged out successfully"));
    }
}
