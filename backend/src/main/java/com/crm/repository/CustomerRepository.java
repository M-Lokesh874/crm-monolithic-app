package com.crm.repository;

import com.crm.entity.Customer;
import com.crm.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Customer entity operations.
 * 
 * This repository provides data access methods for Customer entities, extending
 * JpaRepository to inherit basic CRUD operations. It includes custom query
 * methods for customer-specific business logic, search functionality, and
 * analytics operations.
 * 
 * The repository supports:
 * <ul>
 *   <li>Basic CRUD operations (inherited from JpaRepository)</li>
 *   <li>Custom finder methods using Spring Data JPA naming conventions</li>
 *   <li>Custom JPQL queries for complex search operations</li>
 *   <li>Customer type and status filtering</li>
 *   <li>User assignment filtering</li>
 *   <li>Company name search with case-insensitive matching</li>
 *   <li>Analytics and reporting queries</li>
 * </ul>
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see com.crm.entity.Customer
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Finds customers by company name containing the specified text.
     * 
     * This method performs a case-insensitive search for customers whose
     * company name contains the provided text. Useful for customer lookup
     * and search functionality.
     * 
     * @param companyName The company name text to search for
     * @return List of customers with matching company names
     */
    List<Customer> findByCompanyNameContainingIgnoreCase(String companyName);
    
    /**
     * Finds customers by their type.
     * 
     * This method retrieves all customers that have been assigned the specified
     * customer type. Useful for customer segmentation and reporting.
     * 
     * @param customerType The customer type to filter by
     * @return List of customers with the specified type
     */
    List<Customer> findByCustomerType(Customer.CustomerType customerType);
    
    /**
     * Finds customers by their status.
     * 
     * This method retrieves all customers that have the specified status.
     * Useful for customer lifecycle management and reporting.
     * 
     * @param status The customer status to filter by
     * @return List of customers with the specified status
     */
    List<Customer> findByStatus(Customer.CustomerStatus status);
    
    /**
     * Finds customers assigned to a specific user.
     * 
     * This method retrieves all customers that have been assigned to the
     * user with the specified ID. Useful for user workload management
     * and reporting.
     * 
     * @param userId The ID of the user to filter by
     * @return List of customers assigned to the specified user
     */
    List<Customer> findByAssignedToId(Long userId);
    
    /**
     * Finds customers by industry.
     * 
     * This method retrieves all customers that belong to the specified
     * industry. Useful for industry-specific analysis and reporting.
     * 
     * @param industry The industry to filter by
     * @return List of customers in the specified industry
     */
    List<Customer> findByIndustry(String industry);
    
    /**
     * Searches for customers based on a search term.
     * 
     * This custom query method performs a case-insensitive search across
     * company name, contact person, and email fields. The search is useful
     * for customer lookup and administrative functions.
     * 
     * The search uses the LIKE operator with wildcards to find partial matches.
     * 
     * @param searchTerm The term to search for
     * @return List of customers matching the search criteria
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchCustomers(@Param("searchTerm") String searchTerm);
    
    /**
     * Counts customers by their type.
     * 
     * This method provides a count of customers for each customer type.
     * Useful for customer analytics and reporting dashboards.
     * 
     * @param customerType The customer type to count
     * @return The number of customers with the specified type
     */
    long countByCustomerType(Customer.CustomerType customerType);
    
    /**
     * Counts customers by industry.
     * 
     * This method provides a count of customers for each industry.
     * Useful for industry analysis and reporting dashboards.
     * 
     * @param industry The industry to count
     * @return The number of customers in the specified industry
     */
    @Query("SELECT c.industry, COUNT(c) FROM Customer c GROUP BY c.industry")
    List<Object[]> getCustomerCountByIndustry();
}
