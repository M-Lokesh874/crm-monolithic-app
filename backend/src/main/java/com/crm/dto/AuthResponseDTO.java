package com.crm.dto;

/**
 * Data Transfer Object (DTO) for authentication responses.
 * 
 * This DTO represents the data structure returned after successful user
 * authentication, containing the JWT token and user information needed
 * for subsequent authenticated requests. It's used in the login endpoint
 * to provide authentication results to the client.
 * 
 * The DTO includes:
 * <ul>
 *   <li>JWT token for authenticated session management</li>
 *   <li>Username for user identification</li>
 * </ul>
 * 
 * This response allows the client to store the JWT token and use it
 * for authenticated API calls by including it in the Authorization header.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public class AuthResponseDTO {
    
    /**
     * JWT token for authenticated session management.
     * This token must be included in subsequent API requests
     * in the Authorization header as "Bearer {token}".
     */
    private String token;
    
    /**
     * Username of the authenticated user.
     * Provided for client-side user identification and display.
     */
    private String username;
    
    /**
     * Default constructor.
     * Required for JSON serialization/deserialization.
     */
    public AuthResponseDTO() {}
    
    /**
     * Constructor with token and username.
     * 
     * @param token The JWT token for authentication
     * @param username The username of the authenticated user
     */
    public AuthResponseDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }
    
    // Getters and Setters with Javadoc
    
    /**
     * Gets the JWT token for authentication.
     * 
     * @return The JWT token
     */
    public String getToken() {
        return token;
    }
    
    /**
     * Sets the JWT token for authentication.
     * 
     * @param token The JWT token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
    
    /**
     * Gets the username of the authenticated user.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username of the authenticated user.
     * 
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
