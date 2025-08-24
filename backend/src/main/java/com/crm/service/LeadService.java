package com.crm.service;

import com.crm.dto.LeadDTO;
import com.crm.dto.LeadUpdateDTO;
import com.crm.entity.Lead;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for lead management operations.
 * Provides business logic for creating, updating, deleting, and querying leads.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public interface LeadService {
    
    /**
     * Creates a new lead in the system.
     * 
     * @param lead The lead entity to create
     * @return LeadDTO containing the created lead information
     * @throws BusinessException if lead creation fails
     */
    LeadDTO createLead(Lead lead);
    
    /**
     * Retrieves all leads in the system.
     * 
     * @return List of all leads as DTOs
     */
    List<LeadDTO> getAllLeads();
    
    /**
     * Retrieves a lead by their unique identifier.
     * 
     * @param id The lead's unique identifier
     * @return Optional containing the lead if found
     */
    Optional<LeadDTO> getLeadById(Long id);
    
    /**
     * Updates an existing lead's information.
     * 
     * @param id The lead's unique identifier
     * @param lead The updated lead information
     * @return LeadDTO containing the updated lead
     * @throws ResourceNotFoundException if lead not found
     * @throws BusinessException if update fails
     */
    LeadDTO updateLead(Long id, Lead lead);
    
    /**
     * Partially updates an existing lead's information.
     * Only updates fields that are provided in the DTO.
     * 
     * @param id The lead's unique identifier
     * @param updateDTO The partial update data
     * @return LeadDTO containing the updated lead
     * @throws ResourceNotFoundException if lead not found
     * @throws BusinessException if update fails
     */
    LeadDTO partialUpdateLead(Long id, LeadUpdateDTO updateDTO);
    
    /**
     * Deletes a lead from the system.
     * 
     * @param id The lead's unique identifier
     * @throws ResourceNotFoundException if lead not found
     */
    void deleteLead(Long id);
    
    /**
     * Assigns a lead to a specific user.
     * 
     * @param leadId The lead's unique identifier
     * @param userId The user's unique identifier
     * @return LeadDTO containing the updated lead
     * @throws ResourceNotFoundException if lead or user not found
     */
    LeadDTO assignLeadToUser(Long leadId, Long userId);
    
    /**
     * Updates the status of a lead.
     * 
     * @param leadId The lead's unique identifier
     * @param status The new status to set
     * @return LeadDTO containing the updated lead
     * @throws ResourceNotFoundException if lead not found
     */
    LeadDTO updateLeadStatus(Long leadId, Lead.LeadStatus status);
    
    /**
     * Searches for leads based on a search term.
     * 
     * @param searchTerm The term to search for
     * @return List of leads matching the search criteria
     */
    List<LeadDTO> searchLeads(String searchTerm);
    
    /**
     * Retrieves leads by their status.
     * 
     * @param status The status to filter by
     * @return List of leads with the specified status
     */
    List<LeadDTO> getLeadsByStatus(Lead.LeadStatus status);
    
    /**
     * Retrieves leads by their source.
     * 
     * @param source The source to filter by
     * @return List of leads with the specified source
     */
    List<LeadDTO> getLeadsBySource(Lead.LeadSource source);
    
    /**
     * Retrieves leads assigned to a specific user.
     * 
     * @param userId The user's unique identifier
     * @return List of leads assigned to the user
     */
    List<LeadDTO> getLeadsByAssignedUser(Long userId);
    
    /**
     * Retrieves leads for a specific customer.
     * 
     * @param customerId The customer's unique identifier
     * @return List of leads for the customer
     */
    List<LeadDTO> getLeadsByCustomer(Long customerId);
    
    /**
     * Gets analytics data for leads.
     * 
     * @return Map containing lead analytics data
     */
    java.util.Map<String, Object> getLeadAnalytics();
    
    /**
     * Gets the total count of leads.
     * 
     * @return Total number of leads
     */
    long getTotalLeadCount();
    
    /**
     * Gets lead count by status.
     * 
     * @return Map of status to count
     */
    java.util.Map<Lead.LeadStatus, Long> getLeadCountByStatus();
}
