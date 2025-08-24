package com.crm.dto;

import com.crm.entity.Lead;
import com.crm.entity.User;
import com.crm.entity.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Lead entity.
 * Used to transfer lead data between different layers of the application.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public class LeadDTO {
    
    private Long id;
    private String title;
    private String description;
    private Lead.LeadStatus leadStatus;
    private Lead.LeadSource leadSource;
    private BigDecimal expectedValue;
    private Integer probabilityPercentage;
    private LocalDateTime expectedCloseDate;
    private Long customerId;
    private String customerName;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String notes;
    
    // Default constructor
    public LeadDTO() {}
    
    // Constructor from Lead entity
    public LeadDTO(Lead lead) {
        this.id = lead.getId();
        this.title = lead.getTitle();
        this.description = lead.getDescription();
        this.leadStatus = lead.getLeadStatus();
        this.leadSource = lead.getLeadSource();
        this.expectedValue = lead.getExpectedValue();
        this.probabilityPercentage = lead.getProbabilityPercentage();
        this.expectedCloseDate = lead.getExpectedCloseDate();
        this.notes = lead.getNotes();
        this.createdAt = lead.getCreatedAt();
        this.updatedAt = lead.getUpdatedAt();
        
        if (lead.getCustomer() != null) {
            this.customerId = lead.getCustomer().getId();
            this.customerName = lead.getCustomer().getCompanyName();
        }
        
        if (lead.getAssignedTo() != null) {
            this.assignedToId = lead.getAssignedTo().getId();
            this.assignedToName = lead.getAssignedTo().getFirstName() + " " + lead.getAssignedTo().getLastName();
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Lead.LeadStatus getLeadStatus() {
        return leadStatus;
    }
    
    public void setLeadStatus(Lead.LeadStatus leadStatus) {
        this.leadStatus = leadStatus;
    }
    
    public Lead.LeadSource getLeadSource() {
        return leadSource;
    }
    
    public void setLeadSource(Lead.LeadSource leadSource) {
        this.leadSource = leadSource;
    }
    
    public BigDecimal getExpectedValue() {
        return expectedValue;
    }
    
    public void setExpectedValue(BigDecimal expectedValue) {
        this.expectedValue = expectedValue;
    }
    
    public Integer getProbabilityPercentage() {
        return probabilityPercentage;
    }
    
    public void setProbabilityPercentage(Integer probabilityPercentage) {
        this.probabilityPercentage = probabilityPercentage;
    }
    
    public LocalDateTime getExpectedCloseDate() {
        return expectedCloseDate;
    }
    
    public void setExpectedCloseDate(LocalDateTime expectedCloseDate) {
        this.expectedCloseDate = expectedCloseDate;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public Long getAssignedToId() {
        return assignedToId;
    }
    
    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }
    
    public String getAssignedToName() {
        return assignedToName;
    }
    
    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
