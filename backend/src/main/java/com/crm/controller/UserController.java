package com.crm.controller;

import com.crm.dto.UserDTO;
import com.crm.dto.UserUpdateDTO;
import com.crm.entity.User;
import com.crm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for user management operations.
 * Provides endpoints for CRUD operations, user search, and role-based access control.
 * All endpoints require appropriate authentication and authorization.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Retrieves all users in the system.
     * Requires ADMIN or MANAGER role access.
     * 
     * @return ResponseEntity containing list of all users
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Retrieves a user by their unique identifier.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param id The user's unique identifier
     * @return ResponseEntity containing the user if found, or 404 if not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves a user by their username.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param username The username to search for
     * @return ResponseEntity containing the user if found, or 404 if not found
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Creates a new user in the system.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param user The user entity to create
     * @return ResponseEntity containing the created user, or 400 if creation fails
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User user) {
        try {
            UserDTO createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Updates an existing user's information.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param id The user's unique identifier
     * @param user The updated user information
     * @return ResponseEntity containing the updated user, or 400 if update fails
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            UserDTO updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Partially updates an existing user's information.
     * Only updates fields that are provided in the update DTO.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param id The user's unique identifier
     * @param updateDTO The partial update information
     * @return ResponseEntity containing the updated user, or 400 if update fails
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserDTO> partialUpdateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO updateDTO) {
        try {
            UserDTO updatedUser = userService.partialUpdateUser(id, updateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Soft deletes a user by setting their active status to false.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param id The user's unique identifier
     * @return ResponseEntity with 200 status if deletion successful, or 400 if fails
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Retrieves all users with a specific role.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param role The role to filter by
     * @return ResponseEntity containing list of users with the specified role
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable User.UserRole role) {
        List<UserDTO> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Retrieves all active users in the system.
     * Requires ADMIN or MANAGER role access.
     * 
     * @return ResponseEntity containing list of active users
     */
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserDTO>> getActiveUsers() {
        List<UserDTO> users = userService.getActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Searches for users based on a search term.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param searchTerm The term to search for
     * @return ResponseEntity containing list of users matching the search criteria
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String searchTerm) {
        List<UserDTO> users = userService.searchUsers(searchTerm);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Checks if a username already exists in the system.
     * Public endpoint - no authentication required.
     * 
     * @param username The username to check
     * @return ResponseEntity containing boolean indicating if username exists
     */
    @GetMapping("/check/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Checks if an email already exists in the system.
     * Public endpoint - no authentication required.
     * 
     * @param email The email to check
     * @return ResponseEntity containing boolean indicating if email exists
     */
    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}
