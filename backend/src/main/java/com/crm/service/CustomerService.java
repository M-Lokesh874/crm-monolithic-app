package com.crm.service;

import com.crm.dto.CustomerDTO;
import com.crm.entity.Customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for customer management operations.
 * Provides methods for CRUD operations, customer analytics, and business logic.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public interface CustomerService {
    
    /**
     * Retrieves all customers in the system.
     * 
     * @return List of all customers as DTOs
     */
    List<CustomerDTO> getAllCustomers();
    
    /**
     * Retrieves a customer by their unique identifier.
     * 
     * @param id The customer's unique identifier
     * @return Optional containing the customer DTO if found
     */
    Optional<CustomerDTO> getCustomerById(Long id);
    
    /**
     * Creates a new customer in the system.
     * 
     * @param customer The customer entity to create
     * @return The created customer as a DTO
     */
    CustomerDTO createCustomer(Customer customer);
    
    /**
     * Updates an existing customer's information.
     * 
     * @param id The customer's unique identifier
     * @param customerDetails The updated customer information
     * @return The updated customer as a DTO
     */
    CustomerDTO updateCustomer(Long id, Customer customerDetails);
    
    /**
     * Deletes a customer from the system.
     * 
     * @param id The customer's unique identifier
     */
    void deleteCustomer(Long id);
    
    /**
     * Assigns a customer to a specific user.
     * 
     * @param customerId The customer's unique identifier
     * @param userId The user's unique identifier
     * @return The updated customer as a DTO
     */
    CustomerDTO assignCustomerToUser(Long customerId, Long userId);
    
    /**
     * Searches for customers based on a search term.
     * 
     * @param searchTerm The term to search for
     * @return List of customers matching the search criteria
     */
    List<CustomerDTO> searchCustomers(String searchTerm);
    
    /**
     * Retrieves customers by their type.
     * 
     * @param customerType The type of customer to filter by
     * @return List of customers with the specified type
     */
    List<CustomerDTO> getCustomersByType(Customer.CustomerType customerType);
    
    /**
     * Retrieves customers assigned to a specific user.
     * 
     * @param userId The user's unique identifier
     * @return List of customers assigned to the user
     */
    List<CustomerDTO> getCustomersByAssignedUser(Long userId);
    
    /**
     * Retrieves customers by their status.
     * 
     * @param status The status to filter by
     * @return List of customers with the specified status
     */
    List<CustomerDTO> getCustomersByStatus(Customer.CustomerStatus status);
    
    /**
     * Gets analytics data for customers.
     * 
     * @return Map containing customer analytics data
     */
    Map<String, Object> getCustomerAnalytics();
    
    /**
     * Gets the total count of customers.
     * 
     * @return Total number of customers
     */
    long getTotalCustomerCount();
    
    /**
     * Gets customer count by status.
     * 
     * @return Map of status to count
     */
    Map<Customer.CustomerStatus, Long> getCustomerCountByStatus();
}
