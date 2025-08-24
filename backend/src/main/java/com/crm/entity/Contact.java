package com.crm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Contact type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Type of contact interaction", example = "PHONE_CALL", 
            allowableValues = {"PHONE_CALL", "EMAIL", "MEETING", "DEMO", "PRESENTATION", "FOLLOW_UP", "OTHER"})
    private ContactType contactType;
    
    @NotBlank(message = "Contact subject is required")
    @Column(nullable = false)
    private String subject;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "contact_date", nullable = false)
    private LocalDateTime contactDate;
    
    @Column(name = "next_follow_up")
    private LocalDateTime nextFollowUp;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
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
    public Contact() {}
    
    public Contact(ContactType contactType, String subject, Customer customer, User createdBy) {
        this.contactType = contactType;
        this.subject = subject;
        this.customer = customer;
        this.createdBy = createdBy;
        this.contactDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ContactType getContactType() {
        return contactType;
    }
    
    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getContactDate() {
        return contactDate;
    }
    
    public void setContactDate(LocalDateTime contactDate) {
        this.contactDate = contactDate;
    }
    
    public LocalDateTime getNextFollowUp() {
        return nextFollowUp;
    }
    
    public void setNextFollowUp(LocalDateTime nextFollowUp) {
        this.nextFollowUp = nextFollowUp;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public User getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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
    
    // Enum for Contact Types
    public enum ContactType {
        PHONE_CALL, EMAIL, MEETING, DEMO, PRESENTATION, FOLLOW_UP, OTHER
    }
}
