package com.crm.dto;

import com.crm.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for User entities.
 * 
 * This DTO provides a clean, serializable representation of User data
 * for API responses, excluding sensitive information like passwords
 * and internal system fields. It's used to transfer user data between
 * the backend service layer and the frontend client.
 * 
 * The DTO includes:
 * <ul>
 *   <li>Basic user identification (ID, username, email)</li>
 *   <li>Personal information (first name, last name)</li>
 *   <li>Role and status information</li>
 *   <li>Timestamps for auditing</li>
 * </ul>
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see com.crm.entity.User
 */
public class UserDTO {
    
    /**
     * Unique identifier for the user.
     */
    private Long id;
    
    /**
     * Unique username for authentication and display.
     */
    private String username;
    
    /**
     * User's email address for communication.
     */
    private String email;
    
    /**
     * User's first name.
     */
    private String firstName;
    
    /**
     * User's last name.
     */
    private String lastName;
    
    /**
     * User's role in the system (ADMIN, MANAGER, SALES_REP).
     */
    private User.UserRole role;
    
    /**
     * Flag indicating if the user account is active.
     */
    @JsonProperty("isActive")
    private boolean isActive;
    
    /**
     * Timestamp when the user was created.
     */
    private String createdAt;
    
    /**
     * Timestamp when the user was last updated.
     */
    private String updatedAt;
    
    /**
     * Default constructor.
     * Required for JSON serialization/deserialization.
     */
    public UserDTO() {}
    
    /**
     * Constructor that creates a DTO from a User entity.
     * 
     * This constructor extracts relevant information from a User entity
     * and creates a DTO suitable for API responses. It excludes sensitive
     * information like passwords and converts timestamps to strings.
     * 
     * @param user The User entity to convert to DTO
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();
        this.isActive = user.isActive();
        this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : null;
        this.updatedAt = user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null;
    }
    
    // Getters and Setters with Javadoc
    
    /**
     * Gets the user's unique identifier.
     * 
     * @return The user's ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the user's unique identifier.
     * 
     * @param id The ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the user's username.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the user's username.
     * 
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the user's email address.
     * 
     * @return The email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the user's email address.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the user's first name.
     * 
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the user's first name.
     * 
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Gets the user's last name.
     * 
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Sets the user's last name.
     * 
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Gets the user's role in the system.
     * 
     * @return The user's role
     */
    public User.UserRole getRole() {
        return role;
    }
    
    /**
     * Sets the user's role in the system.
     * 
     * @param role The role to set
     */
    public void setRole(User.UserRole role) {
        this.role = role;
    }
    
    /**
     * Checks if the user account is active.
     * 
     * @return true if the account is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Sets the user account's active status.
     * 
     * @param active The active status to set
     */
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * Gets the timestamp when the user was created.
     * 
     * @return The creation timestamp as a string
     */
    public String getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the timestamp when the user was created.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the timestamp when the user was last updated.
     * 
     * @return The last update timestamp as a string
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Sets the timestamp when the user was last updated.
     * 
     * @param updatedAt The last update timestamp to set
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
