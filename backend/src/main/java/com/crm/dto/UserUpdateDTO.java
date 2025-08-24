package com.crm.dto;

import com.crm.entity.User.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating user information.
 * Allows partial updates without requiring all fields.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@Schema(description = "User update DTO")
public class UserUpdateDTO {
    
    @Schema(description = "User's first name", example = "John")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @Schema(description = "User's last name", example = "Doe")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @Schema(description = "User's email address", example = "john.doe@example.com")
    @Email(message = "Invalid email address")
    private String email;
    
    @Schema(description = "User's role in the system", example = "SALES_REP")
    private UserRole role;
    
    @Schema(description = "Flag indicating if the user account is active", example = "true")
    @JsonProperty("isActive")
    private Boolean isActive;
    
    @Schema(description = "New password (optional, only set if changing password)")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    // Default constructor
    public UserUpdateDTO() {}
    
    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
