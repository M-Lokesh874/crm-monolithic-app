package com.crm.service;

import com.crm.dto.UserDTO;
import com.crm.dto.UserUpdateDTO;
import com.crm.dto.PasswordUpdateDTO;
import com.crm.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for user management operations.
 * Provides methods for CRUD operations, authentication, and user-related business logic.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public interface UserService extends UserDetailsService {
    
    /**
     * Retrieves all users in the system.
     * 
     * @return List of all users as DTOs
     */
    List<UserDTO> getAllUsers();
    
    /**
     * Retrieves a user by their unique identifier.
     * 
     * @param id The user's unique identifier
     * @return Optional containing the user DTO if found
     */
    Optional<UserDTO> getUserById(Long id);
    
    /**
     * Retrieves a user by their username.
     * 
     * @param username The username to search for
     * @return Optional containing the user DTO if found
     */
    Optional<UserDTO> getUserByUsername(String username);
    
    /**
     * Creates a new user in the system.
     * 
     * @param user The user entity to create
     * @return The created user as a DTO
     * @throws RuntimeException if username or email already exists
     */
    UserDTO createUser(User user);
    
    /**
     * Updates an existing user's information.
     * 
     * @param id The user's unique identifier
     * @param userDetails The updated user information
     * @return The updated user as a DTO
     * @throws RuntimeException if user not found
     */
    UserDTO updateUser(Long id, User userDetails);
    
    /**
     * Partially updates an existing user's information.
     * Only updates fields that are provided in the update DTO.
     * 
     * @param id The user's unique identifier
     * @param updateDTO The partial update information
     * @return The updated user as a DTO
     * @throws RuntimeException if user not found
     */
    UserDTO partialUpdateUser(Long id, UserUpdateDTO updateDTO);
    
    /**
     * Soft deletes a user by setting their active status to false.
     * 
     * @param id The user's unique identifier
     * @throws RuntimeException if user not found
     */
    void deleteUser(Long id);
    
    /**
     * Retrieves all users with a specific role.
     * 
     * @param role The role to filter by
     * @return List of users with the specified role
     */
    List<UserDTO> getUsersByRole(User.UserRole role);
    
    /**
     * Retrieves all active users in the system.
     * 
     * @return List of active users
     */
    List<UserDTO> getActiveUsers();
    
    /**
     * Searches for users based on a search term.
     * 
     * @param searchTerm The term to search for
     * @return List of users matching the search criteria
     */
    List<UserDTO> searchUsers(String searchTerm);
    
    /**
     * Checks if a username already exists in the system.
     * 
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Checks if an email already exists in the system.
     * 
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Updates a user's password.
     * 
     * @param username The username of the user
     * @param passwordUpdateDTO The password update data
     * @throws RuntimeException if user not found or current password is incorrect
     */
    void updatePassword(String username, PasswordUpdateDTO passwordUpdateDTO);
}
