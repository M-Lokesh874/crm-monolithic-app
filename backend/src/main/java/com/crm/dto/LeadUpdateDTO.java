package com.crm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for partial lead updates
 * Only validates fields that are actually being updated
 */
@Schema(description = "Lead update request - only include fields to update")
public class LeadUpdateDTO {
    
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Schema(description = "Lead title", example = "Software Implementation Project")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Lead description", example = "Implementation of CRM system")
    private String description;
    
    @Schema(description = "Current status of the lead", example = "CONTACTED", 
            allowableValues = {"NEW", "CONTACTED", "QUALIFIED", "PROPOSAL", "NEGOTIATION", "CLOSED_WON", "CLOSED_LOST"})
    private String leadStatus;
    
    @Schema(description = "Source where the lead originated", example = "WEBSITE", 
            allowableValues = {"WEBSITE", "REFERRAL", "SOCIAL_MEDIA", "EMAIL", "PHONE", "TRADE_SHOW", "OTHER"})
    private String leadSource;
    
    @Positive(message = "Expected value must be positive")
    @Schema(description = "Expected deal value", example = "50000.00")
    private BigDecimal expectedValue;
    
    @Positive(message = "Probability percentage must be positive")
    @Schema(description = "Probability of closing the deal", example = "75")
    private Integer probabilityPercentage;
    
    @Schema(description = "Expected close date", example = "2025-12-31T23:59:59")
    private LocalDateTime expectedCloseDate;
    
    @Schema(description = "Customer ID", example = "1")
    private Long customerId;
    
    @Schema(description = "Assigned user ID", example = "1")
    private Long assignedToId;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    @Schema(description = "Internal notes", example = "Follow up next week")
    private String notes;
    
    // Constructors
    public LeadUpdateDTO() {}
    
    // Getters and Setters
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
    
    public String getLeadStatus() {
        return leadStatus;
    }
    
    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }
    
    public String getLeadSource() {
        return leadSource;
    }
    
    public void setLeadSource(String leadSource) {
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
    
    public Long getAssignedToId() {
        return assignedToId;
    }
    
    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
