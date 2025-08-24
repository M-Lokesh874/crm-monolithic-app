package com.crm.repository;

import com.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * 
 * This repository provides data access methods for User entities, extending
 * JpaRepository to inherit basic CRUD operations. It includes custom query
 * methods for user-specific business logic and search functionality.
 * 
 * The repository supports:
 * <ul>
 *   <li>Basic CRUD operations (inherited from JpaRepository)</li>
 *   <li>Custom finder methods using Spring Data JPA naming conventions</li>
 *   <li>Custom JPQL queries for complex search operations</li>
 *   <li>Role-based user filtering</li>
 *   <li>Active user filtering</li>
 *   <li>Username and email existence checks</li>
 * </ul>
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see com.crm.entity.User
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Finds a user by their username.
     * 
     * This method performs a case-sensitive search for a user with the
     * specified username. Returns an Optional to handle cases where no
     * user is found.
     * 
     * @param username The username to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Finds a user by their email address.
     * 
     * This method performs a case-sensitive search for a user with the
     * specified email address. Returns an Optional to handle cases where no
     * user is found.
     * 
     * @param email The email address to search for
     * @return Optional containing the user if found, empty otherwise
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Checks if a user with the specified username exists.
     * 
     * This method is useful for validation purposes, such as checking
     * username uniqueness during user registration.
     * 
     * @param username The username to check
     * @return true if a user with the username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Checks if a user with the specified email exists.
     * 
     * This method is useful for validation purposes, such as checking
     * email uniqueness during user registration.
     * 
     * @param email The email address to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Finds all users with a specific role.
     * 
     * This method retrieves all users that have been assigned the specified
     * role. Useful for role-based operations and reporting.
     * 
     * @param role The role to filter by
     * @return List of users with the specified role
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * Finds all active users in the system.
     * 
     * This method retrieves all users whose accounts are currently active.
     * Inactive users are excluded from the results.
     * 
     * @return List of all active users
     */
    List<User> findByIsActiveTrue();
    
    /**
     * Searches for users based on a search term.
     * 
     * This custom query method performs a case-insensitive search across
     * username, first name, last name, and email fields. The search is
     * useful for user lookup and administrative functions.
     * 
     * The search uses the LIKE operator with wildcards to find partial matches.
     * 
     * @param searchTerm The term to search for
     * @return List of users matching the search criteria
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
    
    /**
     * Finds users by their active status.
     * 
     * This method allows filtering users by their active/inactive status.
     * Useful for administrative functions and user management.
     * 
     * @param isActive The active status to filter by
     * @return List of users with the specified active status
     */
    List<User> findByIsActive(boolean isActive);
    
    /**
     * Counts users by their role.
     * 
     * This method provides a count of users for each role in the system.
     * Useful for reporting and administrative dashboards.
     * 
     * @param role The role to count
     * @return The number of users with the specified role
     */
    long countByRole(User.UserRole role);
    
    /**
     * Counts active users in the system.
     * 
     * This method provides a count of all currently active users.
     * Useful for system statistics and monitoring.
     * 
     * @return The number of active users
     */
    long countByIsActiveTrue();
}
