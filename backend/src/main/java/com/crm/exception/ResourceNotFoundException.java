package com.crm.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Used to indicate that an entity or resource with the specified identifier
 * does not exist in the system.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new ResourceNotFoundException with the specified message.
     * 
     * @param message The detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new ResourceNotFoundException with the specified message and cause.
     * 
     * @param message The detail message
     * @param cause The cause of the exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new ResourceNotFoundException for a specific resource type and identifier.
     * 
     * @param resourceType The type of resource that was not found
     * @param identifier The identifier that was used to search for the resource
     */
    public ResourceNotFoundException(String resourceType, Object identifier) {
        super(String.format("%s not found with identifier: %s", resourceType, identifier));
    }
}
