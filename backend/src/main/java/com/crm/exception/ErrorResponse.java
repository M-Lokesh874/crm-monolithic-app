package com.crm.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response model for the CRM application.
 * Provides consistent error response structure across all endpoints.
 * Used by the GlobalExceptionHandler to format error responses.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@Builder
public class ErrorResponse {
    
    /**
     * Timestamp when the error occurred.
     */
    private LocalDateTime timestamp;
    
    /**
     * HTTP status code of the error.
     */
    private int status;
    
    /**
     * Short error description.
     */
    private String error;
    
    /**
     * Detailed error message.
     */
    private String message;
    
    /**
     * Request path where the error occurred.
     */
    private String path;
    
    /**
     * Additional error details (e.g., validation errors).
     * Can be null if no additional details are available.
     */
    private Map<String, String> details;
}
