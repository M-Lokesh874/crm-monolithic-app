package com.crm.service;

import com.crm.dto.AuthRequestDTO;
import com.crm.dto.AuthResponseDTO;
import com.crm.dto.UserRegistrationDTO;
import com.crm.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service interface for authentication operations.
 * Provides methods for user authentication, registration, JWT token generation, and validation.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public interface AuthService {
    
    /**
     * Registers a new user in the system.
     * 
     * @param registrationRequest The user registration request
     * @return Authentication response with JWT token and user details
     * @throws RuntimeException if registration fails
     */
    AuthResponseDTO registerUser(UserRegistrationDTO registrationRequest);
    
    /**
     * Authenticates a user with the provided credentials.
     * 
     * @param authRequest The authentication request containing username and password
     * @return Authentication response with JWT token and user details
     * @throws RuntimeException if authentication fails
     */
    AuthResponseDTO authenticateUser(AuthRequestDTO authRequest);
    
    /**
     * Validates a JWT token and returns the associated user details.
     * 
     * @param token The JWT token to validate
     * @return User details if token is valid
     * @throws RuntimeException if token is invalid or expired
     */
    UserDetails validateToken(String token);
    
    /**
     * Generates a new JWT token for a user.
     * 
     * @param user The user for whom to generate the token
     * @return The generated JWT token
     */
    String generateToken(User user);
}
