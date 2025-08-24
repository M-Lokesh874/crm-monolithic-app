package com.crm.controller;

import com.crm.dto.LeadDTO;
import com.crm.dto.LeadUpdateDTO;
import com.crm.entity.Lead;
import com.crm.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for lead management operations.
 * Provides endpoints for CRUD operations, lead search, analytics, and role-based access control.
 * All endpoints require appropriate authentication and authorization.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/leads")
@CrossOrigin(origins = "*")
public class LeadController {
    
    @Autowired
    private LeadService leadService;
    
    /**
     * Retrieves all leads in the system.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @return ResponseEntity containing list of all leads
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<LeadDTO>> getAllLeads() {
        List<LeadDTO> leads = leadService.getAllLeads();
        return ResponseEntity.ok(leads);
    }
    
    /**
     * Retrieves a lead by their unique identifier.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param id The lead's unique identifier
     * @return ResponseEntity containing the lead if found, or 404 if not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<LeadDTO> getLeadById(@PathVariable Long id) {
        return leadService.getLeadById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Creates a new lead in the system.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param lead The lead entity to create
     * @return ResponseEntity containing the created lead
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<LeadDTO> createLead(@Valid @RequestBody Lead lead) {
        // Set default values if not provided
        if (lead.getLeadStatus() == null) {
            lead.setLeadStatus(Lead.LeadStatus.NEW);
        }
        
        LeadDTO createdLead = leadService.createLead(lead);
        return ResponseEntity.ok(createdLead);
    }
    
    /**
     * Updates an existing lead's information.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param id The lead's unique identifier
     * @param lead The updated lead information
     * @return ResponseEntity containing the updated lead, or 400 if update fails
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<LeadDTO> updateLead(@PathVariable Long id, @Valid @RequestBody Lead lead) {
        try {
            LeadDTO updatedLead = leadService.updateLead(id, lead);
            return ResponseEntity.ok(updatedLead);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Partially updates an existing lead's information.
     * Only validates fields that are actually being updated.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param id The lead's unique identifier
     * @param updateDTO The partial update data
     * @return ResponseEntity containing the updated lead, or 400 if update fails
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<LeadDTO> partialUpdateLead(@PathVariable Long id, @Valid @RequestBody LeadUpdateDTO updateDTO) {
        try {
            LeadDTO updatedLead = leadService.partialUpdateLead(id, updateDTO);
            return ResponseEntity.ok(updatedLead);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Deletes a lead from the system.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param id The lead's unique identifier
     * @return ResponseEntity with 200 status if deletion successful, or 400 if fails
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        try {
            leadService.deleteLead(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Assigns a lead to a specific user.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param leadId The lead's unique identifier
     * @param userId The user's unique identifier
     * @return ResponseEntity containing the updated lead, or 400 if assignment fails
     */
    @PutMapping("/{leadId}/assign/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<LeadDTO> assignLeadToUser(@PathVariable Long leadId, @PathVariable Long userId) {
        try {
            LeadDTO updatedLead = leadService.assignLeadToUser(leadId, userId);
            return ResponseEntity.ok(updatedLead);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Updates the status of a lead.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param leadId The lead's unique identifier
     * @param status The new status to set
     * @return ResponseEntity containing the updated lead, or 400 if update fails
     */
    @PutMapping("/{leadId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<LeadDTO> updateLeadStatus(@PathVariable Long leadId, @RequestBody Map<String, String> request) {
        try {
            String statusStr = request.get("status");
            Lead.LeadStatus status = Lead.LeadStatus.valueOf(statusStr);
            LeadDTO updatedLead = leadService.updateLeadStatus(leadId, status);
            return ResponseEntity.ok(updatedLead);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Searches for leads based on a search term.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param searchTerm The term to search for
     * @return ResponseEntity containing list of leads matching the search criteria
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<LeadDTO>> searchLeads(@RequestParam String searchTerm) {
        List<LeadDTO> leads = leadService.searchLeads(searchTerm);
        return ResponseEntity.ok(leads);
    }
    
    /**
     * Retrieves leads by their status.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param status The status to filter by
     * @return ResponseEntity containing list of leads with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<LeadDTO>> getLeadsByStatus(@PathVariable Lead.LeadStatus status) {
        List<LeadDTO> leads = leadService.getLeadsByStatus(status);
        return ResponseEntity.ok(leads);
    }
    
    /**
     * Retrieves leads by their source.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param source The source to filter by
     * @return ResponseEntity containing list of leads with the specified source
     */
    @GetMapping("/source/{source}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<LeadDTO>> getLeadsBySource(@PathVariable Lead.LeadSource source) {
        List<LeadDTO> leads = leadService.getLeadsBySource(source);
        return ResponseEntity.ok(leads);
    }
    
    /**
     * Retrieves leads assigned to a specific user.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param userId The user's unique identifier
     * @return ResponseEntity containing list of leads assigned to the user
     */
    @GetMapping("/assigned/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<LeadDTO>> getLeadsByAssignedUser(@PathVariable Long userId) {
        List<LeadDTO> leads = leadService.getLeadsByAssignedUser(userId);
        return ResponseEntity.ok(leads);
    }
    
    /**
     * Retrieves leads for a specific customer.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param customerId The customer's unique identifier
     * @return ResponseEntity containing list of leads for the customer
     */
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<LeadDTO>> getLeadsByCustomer(@PathVariable Long customerId) {
        List<LeadDTO> leads = leadService.getLeadsByCustomer(customerId);
        return ResponseEntity.ok(leads);
    }
    
    /**
     * Gets analytics data for leads.
     * Requires ADMIN or MANAGER role access.
     * 
     * @return ResponseEntity containing lead analytics data
     */
    @GetMapping("/analytics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, Object>> getLeadAnalytics() {
        Map<String, Object> analytics = leadService.getLeadAnalytics();
        return ResponseEntity.ok(analytics);
    }
}
