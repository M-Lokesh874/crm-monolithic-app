package com.crm.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for authentication requests.
 * 
 * This DTO represents the data structure required for user authentication,
 * containing the credentials needed to log into the CRM system. It's used
 * in the login endpoint to receive user authentication requests.
 * 
 * The DTO includes:
 * <ul>
 *   <li>Username for user identification</li>
 *   <li>Password for authentication verification</li>
 * </ul>
 * 
 * Validation annotations ensure that both fields are provided before
 * processing the authentication request.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public class AuthRequestDTO {
    
    /**
     * Username for authentication.
     * Must not be blank and is used to identify the user.
     */
    @NotBlank(message = "Username is required")
    private String username;
    
    /**
     * Password for authentication.
     * Must not be blank and is used to verify user identity.
     */
    @NotBlank(message = "Password is required")
    private String password;
    
    /**
     * Default constructor.
     * Required for JSON serialization/deserialization.
     */
    public AuthRequestDTO() {}
    
    /**
     * Constructor with username and password.
     * 
     * @param username The username for authentication
     * @param password The password for authentication
     */
    public AuthRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters with Javadoc
    
    /**
     * Gets the username for authentication.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username for authentication.
     * 
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the password for authentication.
     * 
     * @return The password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the password for authentication.
     * 
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
