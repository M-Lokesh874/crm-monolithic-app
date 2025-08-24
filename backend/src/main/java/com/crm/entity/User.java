package com.crm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Entity class representing a user in the CRM system.
 * Implements UserDetails interface for Spring Security integration.
 * Contains user authentication and profile information.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "users")
@Schema(description = "User entity")
public class User implements UserDetails {
    
    /**
     * Unique identifier for the user.
     * Auto-generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the user", example = "1")
    private Long id;
    
    /**
     * Unique username for authentication.
     * Must be unique across all users.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Unique username for authentication", example = "john_doe")
    private String username;
    
    /**
     * User's email address.
     * Must be unique across all users.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email address")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;
    
    /**
     * Encrypted password for authentication.
     * Stored in encrypted format using Spring Security's PasswordEncoder.
     */
    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Encrypted password for authentication", example = "encrypted_password")
    private String password;
    
    /**
     * User's first name.
     */
    @Column(name = "first_name")
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;
    
    /**
     * User's last name.
     */
    @Column(name = "last_name")
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;
    
    /**
     * User's role in the system.
     * Determines access permissions and capabilities.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "User's role in the system", example = "ADMIN", 
            allowableValues = {"ADMIN", "MANAGER", "SALES_REP"})
    private UserRole role;
    
    /**
     * Flag indicating if the user account is active.
     * Inactive users cannot authenticate.
     */
    @Column(name = "is_active")
    @Schema(description = "Flag indicating if the user account is active", example = "true")
    @JsonProperty("isActive")
    private boolean isActive = true;
    
    /**
     * Timestamp when the user was created.
     * Auto-set on entity creation.
     */
    @Column(name = "created_at")
    @Schema(description = "Timestamp when the user was created", example = "2023-10-27T10:00:00")
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the user was last updated.
     * Auto-updated on entity modification.
     */
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the user was last updated", example = "2023-10-27T10:00:00")
    private LocalDateTime updatedAt;
    
    /**
     * Enumeration of possible user roles in the system.
     * Each role has different access permissions.
     */
    public enum UserRole {
        /** Administrator with full system access */
        ADMIN,
        /** Manager with elevated access to team resources */
        MANAGER,
        /** Sales representative with customer management access */
        SALES_REP
    }
    
    /**
     * Default constructor.
     * Initializes timestamps.
     */
    public User() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Constructor with basic user information.
     * 
     * @param username Unique username for the user
     * @param email User's email address
     * @param password User's password (will be encrypted)
     * @param firstName User's first name
     * @param lastName User's last name
     * @param role User's role in the system
     */
    public User(String username, String email, String password, String firstName, String lastName, UserRole role) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
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
    @Override
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
     * Gets the user's encrypted password.
     * 
     * @return The encrypted password
     */
    @Override
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the user's password.
     * Note: This should be encrypted before setting.
     * 
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
    public UserRole getRole() {
        return role;
    }
    
    /**
     * Sets the user's role in the system.
     * 
     * @param role The role to set
     */
    public void setRole(UserRole role) {
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
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the timestamp when the user was created.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the timestamp when the user was last updated.
     * 
     * @return The last update timestamp
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Sets the timestamp when the user was last updated.
     * 
     * @param updatedAt The last update timestamp to set
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Spring Security UserDetails implementation
    
    /**
     * Returns the authorities granted to the user.
     * Based on the user's role in the system.
     * 
     * @return Collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    
    /**
     * Indicates whether the user's account has expired.
     * 
     * @return true if the account is non-expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    /**
     * Indicates whether the user's account is locked.
     * 
     * @return true if the account is non-locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    /**
     * Indicates whether the user's credentials have expired.
     * 
     * @return true if the credentials are non-expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    /**
     * Indicates whether the user is enabled.
     * 
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return isActive;
    }
    
    /**
     * Pre-persist callback to set creation timestamp.
     * Automatically called before entity is persisted.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Pre-update callback to set update timestamp.
     * Automatically called before entity is updated.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
