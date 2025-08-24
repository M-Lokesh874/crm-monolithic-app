package com.crm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "leads")
public class Lead {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Lead title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(nullable = false)
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Lead status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "lead_status", nullable = false)
    @Schema(description = "Current status of the lead", example = "NEW", 
            allowableValues = {"NEW", "CONTACTED", "QUALIFIED", "PROPOSAL", "NEGOTIATION", "CLOSED_WON", "CLOSED_LOST"})
    private LeadStatus leadStatus;
    
    @NotNull(message = "Lead source is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "lead_source", nullable = false)
    @Schema(description = "Source where the lead originated", example = "WEBSITE", 
            allowableValues = {"WEBSITE", "REFERRAL", "SOCIAL_MEDIA", "EMAIL", "PHONE", "TRADE_SHOW", "OTHER"})
    private LeadSource leadSource;
    
    @Positive(message = "Expected value must be positive")
    @Column(name = "expected_value")
    private BigDecimal expectedValue;
    
    @Positive(message = "Probability percentage must be positive")
    @Column(name = "probability_percentage")
    private Integer probabilityPercentage;
    
    @Column(name = "expected_close_date")
    private LocalDateTime expectedCloseDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Lead() {}
    
    public Lead(String title, LeadSource leadSource, Customer customer) {
        this.title = title;
        this.leadSource = leadSource;
        this.customer = customer;
        this.leadStatus = LeadStatus.NEW;
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
    
    public LeadStatus getLeadStatus() {
        return leadStatus;
    }
    
    public void setLeadStatus(LeadStatus leadStatus) {
        this.leadStatus = leadStatus;
    }
    
    public LeadSource getLeadSource() {
        return leadSource;
    }
    
    public void setLeadSource(LeadSource leadSource) {
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
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public User getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
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
    
    // Enums
    public enum LeadStatus {
        NEW, CONTACTED, QUALIFIED, PROPOSAL, NEGOTIATION, CLOSED_WON, CLOSED_LOST
    }
    
    public enum LeadSource {
        WEBSITE, REFERRAL, SOCIAL_MEDIA, EMAIL, PHONE, TRADE_SHOW, OTHER
    }
}
