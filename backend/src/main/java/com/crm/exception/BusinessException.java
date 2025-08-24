package com.crm.exception;

/**
 * Exception thrown when a business rule is violated.
 * Used to indicate that an operation cannot be completed due to
 * business logic constraints or validation failures.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public class BusinessException extends RuntimeException {
    
    /**
     * Constructs a new BusinessException with the specified message.
     * 
     * @param message The detail message describing the business rule violation
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new BusinessException with the specified message and cause.
     * 
     * @param message The detail message describing the business rule violation
     * @param cause The cause of the exception
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new BusinessException for a specific business rule violation.
     * 
     * @param rule The business rule that was violated
     * @param details Additional details about the violation
     */
    public BusinessException(String rule, String details) {
        super(String.format("Business rule violation - %s: %s", rule, details));
    }
}
