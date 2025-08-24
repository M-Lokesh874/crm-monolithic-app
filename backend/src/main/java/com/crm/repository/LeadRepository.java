package com.crm.repository;

import com.crm.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Lead entity operations.
 * 
 * This repository provides data access methods for Lead entities, extending
 * JpaRepository to inherit basic CRUD operations. It includes custom query
 * methods for lead-specific business logic, pipeline management, and
 * analytics operations.
 * 
 * The repository supports:
 * <ul>
 *   <li>Basic CRUD operations (inherited from JpaRepository)</li>
 *   <li>Custom finder methods using Spring Data JPA naming conventions</li>
 *   <li>Custom JPQL queries for complex search operations</li>
 *   <li>Lead status and source filtering</li>
 *   <li>Customer association filtering</li>
 *   <li>User assignment filtering</li>
 *   <li>Pipeline analytics and reporting</li>
 * </ul>
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see com.crm.entity.Lead
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 */
@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    
    /**
     * Finds leads by their status.
     * 
     * This method retrieves all leads that have the specified status.
     * Useful for pipeline management and lead lifecycle tracking.
     * 
     * @param leadStatus The lead status to filter by
     * @return List of leads with the specified status
     */
    List<Lead> findByLeadStatus(Lead.LeadStatus leadStatus);
    
    /**
     * Finds leads by their source.
     * 
     * This method retrieves all leads that originated from the specified
     * source. Useful for lead source analysis and marketing effectiveness.
     * 
     * @param leadSource The lead source to filter by
     * @return List of leads from the specified source
     */
    List<Lead> findByLeadSource(Lead.LeadSource leadSource);
    
    /**
     * Finds leads associated with a specific customer.
     * 
     * This method retrieves all leads that are associated with the
     * customer having the specified ID. Useful for customer relationship
     * analysis and lead history tracking.
     * 
     * @param customerId The customer ID to filter by
     * @return List of leads associated with the specified customer
     */
    List<Lead> findByCustomerId(Long customerId);
    
    /**
     * Finds leads assigned to a specific user.
     * 
     * This method retrieves all leads that have been assigned to the
     * user with the specified ID. Useful for user workload management
     * and performance tracking.
     * 
     * @param userId The ID of the user to filter by
     * @return List of leads assigned to the specified user
     */
    List<Lead> findByAssignedToId(Long userId);
    
    /**
     * Gets lead count by status for analytics.
     * 
     * This custom query method returns aggregated data showing the count
     * of leads grouped by status. Useful for creating pipeline charts
     * and reports in the dashboard.
     * 
     * @return List of Object arrays containing lead status and count
     */
    @Query("SELECT l.leadStatus, COUNT(l) FROM Lead l GROUP BY l.leadStatus")
    List<Object[]> getLeadCountByStatus();
    
    /**
     * Gets lead count by source for analytics.
     * 
     * This custom query method returns aggregated data showing the count
     * of leads grouped by source. Useful for marketing effectiveness
     * analysis and reporting.
     * 
     * @return List of Object arrays containing lead source and count
     */
    @Query("SELECT l.leadSource, COUNT(l) FROM Lead l GROUP BY l.leadSource")
    List<Object[]> getLeadCountBySource();
    
    /**
     * Counts leads by their status.
     * 
     * This method provides a count of leads for each lead status.
     * Useful for pipeline analytics and reporting dashboards.
     * 
     * @param leadStatus The lead status to count
     * @return The number of leads with the specified status
     */
    long countByLeadStatus(Lead.LeadStatus leadStatus);
    
    /**
     * Counts leads by their source.
     * 
     * This method provides a count of leads for each lead source.
     * Useful for marketing analytics and source effectiveness reporting.
     * 
     * @param leadSource The lead source to count
     * @return The number of leads from the specified source
     */
    long countByLeadSource(Lead.LeadSource leadSource);
    
    /**
     * Counts leads assigned to a specific user.
     * 
     * This method provides a count of leads assigned to the user
     * with the specified ID. Useful for workload analysis and reporting.
     * 
     * @param userId The ID of the user to count leads for
     * @return The number of leads assigned to the specified user
     */
    long countByAssignedToId(Long userId);
    
    /**
     * Finds leads with expected value above a threshold.
     * 
     * This method retrieves all leads that have an expected value
     * greater than or equal to the specified threshold. Useful for
     * high-value lead prioritization and reporting.
     * 
     * @param threshold The minimum expected value threshold
     * @return List of leads with expected value above the threshold
     */
    List<Lead> findByExpectedValueGreaterThanEqual(Double threshold);
    
    /**
     * Searches leads by title or description containing the search term.
     * 
     * This method performs a case-insensitive search across lead titles
     * and descriptions. Useful for implementing search functionality
     * in the lead management interface.
     * 
     * @param title The title search term
     * @param description The description search term
     * @return List of leads matching the search criteria
     */
    List<Lead> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
