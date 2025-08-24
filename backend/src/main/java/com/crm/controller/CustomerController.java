package com.crm.controller;

import com.crm.dto.CustomerDTO;
import com.crm.entity.Customer;
import com.crm.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * REST Controller for customer management operations.
 * Provides endpoints for CRUD operations, customer search, analytics, and role-based access control.
 * All endpoints require appropriate authentication and authorization.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * Retrieves all customers in the system.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @return ResponseEntity containing list of all customers
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    
    /**
     * Retrieves a customer by their unique identifier.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param id The customer's unique identifier
     * @return ResponseEntity containing the customer if found, or 404 if not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Creates a new customer in the system.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param customer The customer entity to create
     * @return ResponseEntity containing the created customer, or 400 if creation fails
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            CustomerDTO createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.ok(createdCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Updates an existing customer's information.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param id The customer's unique identifier
     * @param customer The updated customer information
     * @return ResponseEntity containing the updated customer, or 400 if update fails
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        try {
            CustomerDTO updatedCustomer = customerService.updateCustomer(id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Deletes a customer from the system.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param id The customer's unique identifier
     * @return ResponseEntity with 200 status if deletion successful, or 400 if fails
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Assigns a customer to a specific user.
     * Requires ADMIN or MANAGER role access.
     * 
     * @param customerId The customer's unique identifier
     * @param userId The user's unique identifier
     * @return ResponseEntity containing the updated customer, or 400 if assignment fails
     */
    @PutMapping("/{customerId}/assign/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<CustomerDTO> assignCustomerToUser(@PathVariable Long customerId, @PathVariable Long userId) {
        try {
            CustomerDTO updatedCustomer = customerService.assignCustomerToUser(customerId, userId);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Searches for customers based on a search term.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param searchTerm The term to search for
     * @return ResponseEntity containing list of customers matching the search criteria
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String searchTerm) {
        List<CustomerDTO> customers = customerService.searchCustomers(searchTerm);
        return ResponseEntity.ok(customers);
    }
    
    /**
     * Retrieves customers by their type.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param customerType The type of customer to filter by
     * @return ResponseEntity containing list of customers with the specified type
     */
    @GetMapping("/type/{customerType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<CustomerDTO>> getCustomersByType(@PathVariable Customer.CustomerType customerType) {
        List<CustomerDTO> customers = customerService.getCustomersByType(customerType);
        return ResponseEntity.ok(customers);
    }
    
    /**
     * Retrieves customers assigned to a specific user.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param userId The user's unique identifier
     * @return ResponseEntity containing list of customers assigned to the user
     */
    @GetMapping("/assigned/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<CustomerDTO>> getCustomersByAssignedUser(@PathVariable Long userId) {
        List<CustomerDTO> customers = customerService.getCustomersByAssignedUser(userId);
        return ResponseEntity.ok(customers);
    }
    
    /**
     * Retrieves customers by their status.
     * Requires ADMIN, MANAGER, or SALES_REP role access.
     * 
     * @param status The status to filter by
     * @return ResponseEntity containing list of customers with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    public ResponseEntity<List<CustomerDTO>> getCustomersByStatus(@PathVariable Customer.CustomerStatus status) {
        List<CustomerDTO> customers = customerService.getCustomersByStatus(status);
        return ResponseEntity.ok(customers);
    }
    
    /**
     * Gets analytics data for customers.
     * Requires ADMIN or MANAGER role access.
     * 
     * @return ResponseEntity containing customer analytics data
     */
    @GetMapping("/analytics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, Object>> getCustomerAnalytics() {
        long totalCount = customerService.getTotalCustomerCount();
        Map<Customer.CustomerStatus, Long> statusCount = customerService.getCustomerCountByStatus();
        
        Map<String, Object> analytics = Map.of(
            "totalCustomers", totalCount,
            "customersByStatus", statusCount
        );
        
        return ResponseEntity.ok(analytics);
    }
    
    /**
     * Gets valid enum values for customer creation.
     * This endpoint helps frontend developers understand what values are acceptable
     * for customer type, status, and other enum fields.
     * 
     * @return Map containing valid enum values for customer fields
     */
    @GetMapping("/enums")
    public ResponseEntity<Map<String, Object>> getCustomerEnums() {
        Map<String, Object> enums = new HashMap<>();
        
        // Customer types
        enums.put("customerTypes", Arrays.stream(Customer.CustomerType.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
        
        // Customer statuses
        enums.put("customerStatuses", Arrays.stream(Customer.CustomerStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
        
        // Common industries (suggested values)
        enums.put("suggestedIndustries", Arrays.asList(
            "Technology", "Healthcare", "Finance", "Manufacturing", "Retail", 
            "Education", "Real Estate", "Consulting", "Marketing", "Other"
        ));
        
        return ResponseEntity.ok(enums);
    }
    
    /**
     * Gets all valid enum values for the entire CRM system.
     * This comprehensive endpoint provides all enum values for frontend development.
     * 
     * @return Map containing all valid enum values across all entities
     */
    @GetMapping("/system-enums")
    public ResponseEntity<Map<String, Object>> getSystemEnums() {
        Map<String, Object> systemEnums = new HashMap<>();
        
        // Customer enums
        systemEnums.put("customerTypes", Arrays.stream(Customer.CustomerType.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
        systemEnums.put("customerStatuses", Arrays.stream(Customer.CustomerStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList()));
        
        // User enums
        systemEnums.put("userRoles", Arrays.asList("ADMIN", "MANAGER", "SALES_REP"));
        
        // Lead enums
        systemEnums.put("leadStatuses", Arrays.asList("NEW", "CONTACTED", "QUALIFIED", "PROPOSAL", "NEGOTIATION", "CLOSED_WON", "CLOSED_LOST"));
        systemEnums.put("leadSources", Arrays.asList("WEBSITE", "REFERRAL", "SOCIAL_MEDIA", "EMAIL", "PHONE", "TRADE_SHOW", "OTHER"));
        
        // Task enums
        systemEnums.put("taskPriorities", Arrays.asList("LOW", "MEDIUM", "HIGH", "URGENT"));
        systemEnums.put("taskStatuses", Arrays.asList("PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"));
        systemEnums.put("taskTypes", Arrays.asList("FOLLOW_UP", "DEMO", "PRESENTATION", "PROPOSAL", "NEGOTIATION", "CLOSING", "OTHER"));
        
        // Contact enums
        systemEnums.put("contactTypes", Arrays.asList("PHONE_CALL", "EMAIL", "MEETING", "DEMO", "PRESENTATION", "FOLLOW_UP", "OTHER"));
        
        // Common industries
        systemEnums.put("suggestedIndustries", Arrays.asList(
            "Technology", "Healthcare", "Finance", "Manufacturing", "Retail", 
            "Education", "Real Estate", "Consulting", "Marketing", "Other"
        ));
        
        return ResponseEntity.ok(systemEnums);
    }
}
