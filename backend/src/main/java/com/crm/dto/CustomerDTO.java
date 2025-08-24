package com.crm.dto;

import com.crm.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for Customer entities.
 * 
 * This DTO provides a clean, serializable representation of Customer data
 * for API responses, including essential customer information and
 * relationship data. It's used to transfer customer data between the
 * backend service layer and the frontend client.
 * 
 * The DTO includes:
 * <ul>
 *   <li>Basic customer identification (ID, company name, contact person)</li>
 *   <li>Contact information (email, phone number)</li>
 *   <li>Business classification (type, status, industry)</li>
 *   <li>Assignment information (assigned user)</li>
 *   <li>Timestamps for auditing</li>
 * </ul>
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see com.crm.entity.Customer
 */
public class CustomerDTO {
    
    /**
     * Unique identifier for the customer.
     */
    private Long id;
    
    /**
     * Company name of the customer.
     */
    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name must not exceed 100 characters")
    private String companyName;

    /**
     * Primary contact person at the customer company.
     */
    @NotBlank(message = "Contact person name is required")
    @Size(max = 100, message = "Contact person name must not exceed 100 characters")
    private String contactPerson;
    
    /**
     * Customer's email address for communication.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    /**
     * Customer's phone number for contact.
     */
    private String phoneNumber;
    
    /**
     * Type of customer (PROSPECT, CUSTOMER, PARTNER).
     */
    private Customer.CustomerType customerType;
    
    /**
     * Current status of the customer (ACTIVE, INACTIVE, SUSPENDED).
     */
    private Customer.CustomerStatus status;
    
    /**
     * Industry sector the customer operates in.
     */
    private String industry;
    
    /**
     * ID of the user assigned to manage this customer.
     */
    private Long assignedToId;
    
    /**
     * Name of the user assigned to manage this customer.
     */
    private String assignedToName;
    
    /**
     * Timestamp when the customer was created.
     */
    private String createdAt;
    
    /**
     * Timestamp when the customer was last updated.
     */
    private String updatedAt;
    
    /**
     * Default constructor.
     * Required for JSON serialization/deserialization.
     */
    public CustomerDTO() {}
    
    /**
     * Constructor that creates a DTO from a Customer entity.
     * 
     * This constructor extracts relevant information from a Customer entity
     * and creates a DTO suitable for API responses. It includes assignment
     * information and converts timestamps to strings.
     * 
     * @param customer The Customer entity to convert to DTO
     */
    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.companyName = customer.getCompanyName();
        this.contactPerson = customer.getContactPerson();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.customerType = customer.getCustomerType();
        this.status = customer.getStatus();
        this.industry = customer.getIndustry();
        
        if (customer.getAssignedTo() != null) {
            this.assignedToId = customer.getAssignedTo().getId();
            this.assignedToName = customer.getAssignedTo().getFirstName() + " " + 
                                 customer.getAssignedTo().getLastName();
        }
        
        this.createdAt = customer.getCreatedAt() != null ? customer.getCreatedAt().toString() : null;
        this.updatedAt = customer.getUpdatedAt() != null ? customer.getUpdatedAt().toString() : null;
    }
    
    // Getters and Setters with Javadoc
    
    /**
     * Gets the customer's unique identifier.
     * 
     * @return The customer's ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Sets the customer's unique identifier.
     * 
     * @param id The ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Gets the company name.
     * 
     * @return The company name
     */
    public String getCompanyName() {
        return companyName;
    }
    
    /**
     * Sets the company name.
     * 
     * @param companyName The company name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    /**
     * Gets the primary contact person.
     * 
     * @return The contact person's name
     */
    public String getContactPerson() {
        return contactPerson;
    }
    
    /**
     * Sets the primary contact person.
     * 
     * @param contactPerson The contact person's name to set
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    /**
     * Gets the customer's email address.
     * 
     * @return The email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the customer's email address.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the customer's phone number.
     * 
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    /**
     * Sets the customer's phone number.
     * 
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Gets the customer type.
     * 
     * @return The customer type
     */
    public Customer.CustomerType getCustomerType() {
        return customerType;
    }
    
    /**
     * Sets the customer type.
     * 
     * @param customerType The customer type to set
     */
    public void setCustomerType(Customer.CustomerType customerType) {
        this.customerType = customerType;
    }
    
    /**
     * Gets the customer status.
     * 
     * @return The customer status
     */
    public Customer.CustomerStatus getStatus() {
        return status;
    }
    
    /**
     * Sets the customer status.
     * 
     * @param status The customer status to set
     */
    public void setStatus(Customer.CustomerStatus status) {
        this.status = status;
    }
    
    /**
     * Gets the customer's industry.
     * 
     * @return The industry
     */
    public String getIndustry() {
        return industry;
    }
    
    /**
     * Sets the customer's industry.
     * 
     * @param industry The industry to set
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    /**
     * Gets the ID of the assigned user.
     * 
     * @return The assigned user's ID
     */
    public Long getAssignedToId() {
        return assignedToId;
    }
    
    /**
     * Sets the ID of the assigned user.
     * 
     * @param assignedToId The assigned user's ID to set
     */
    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }
    
    /**
     * Gets the name of the assigned user.
     * 
     * @return The assigned user's name
     */
    public String getAssignedToName() {
        return assignedToName;
    }
    
    /**
     * Sets the name of the assigned user.
     * 
     * @param assignedToName The assigned user's name to set
     */
    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }
    
    /**
     * Gets the timestamp when the customer was created.
     * 
     * @return The creation timestamp as a string
     */
    public String getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the timestamp when the customer was created.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the timestamp when the customer was last updated.
     * 
     * @return The last update timestamp as a string
     */
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Sets the timestamp when the customer was last updated.
     * 
     * @param updatedAt The last update timestamp to set
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
