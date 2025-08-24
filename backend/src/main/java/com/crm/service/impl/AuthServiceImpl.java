package com.crm.service.impl;

import com.crm.dto.AuthRequestDTO;
import com.crm.dto.AuthResponseDTO;
import com.crm.dto.UserRegistrationDTO;
import com.crm.entity.User;
import com.crm.service.AuthService;
import com.crm.service.EmailService;
import com.crm.service.UserService;
import com.crm.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.crm.dto.UserDTO;

/**
 * Implementation of AuthService interface.
 * Provides business logic for authentication operations.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public AuthResponseDTO registerUser(UserRegistrationDTO registrationRequest) {
        try {
            // Check if username already exists
            if (userService.existsByUsername(registrationRequest.getUsername())) {
                throw new RuntimeException("Username already exists: " + registrationRequest.getUsername());
            }
            
            // Check if email already exists
            if (userService.existsByEmail(registrationRequest.getEmail())) {
                throw new RuntimeException("Email already exists: " + registrationRequest.getEmail());
            }
            
            // Create new user
            User newUser = new User();
            newUser.setUsername(registrationRequest.getUsername());
            newUser.setEmail(registrationRequest.getEmail());
            newUser.setPassword(registrationRequest.getPassword()); // Will be encoded in UserService
            newUser.setFirstName(registrationRequest.getFirstName());
            newUser.setLastName(registrationRequest.getLastName());
            newUser.setRole(User.UserRole.SALES_REP); // Default role for new registrations
            newUser.setActive(true);
            
            // Save user (password will be encoded in UserService)
            UserDTO savedUserDTO = userService.createUser(newUser);
            
            // Send welcome email with plain password (using the original user object)
            emailService.sendWelcomeEmail(newUser, registrationRequest.getPassword());
            
            // Send admin notification (using the original user object)
            emailService.sendAdminNotificationEmail(newUser);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(savedUserDTO.getUsername());
            
            return new AuthResponseDTO(token, savedUserDTO.getUsername());
            
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    
    @Override
    public AuthResponseDTO authenticateUser(AuthRequestDTO authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), 
                    authRequest.getPassword()
                )
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());
            
            return new AuthResponseDTO(token, userDetails.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
    
    @Override
    public UserDetails validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            if (username != null && jwtUtil.validateToken(token, username)) {
                return userService.loadUserByUsername(username);
            }
            throw new RuntimeException("Invalid token");
        } catch (Exception e) {
            throw new RuntimeException("Token validation failed: " + e.getMessage());
        }
    }
    
    @Override
    public String generateToken(User user) {
        return jwtUtil.generateToken(user.getUsername());
    }
}
