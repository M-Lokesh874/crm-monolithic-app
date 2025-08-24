package com.crm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the CRM application.
 * Provides centralized exception handling across all controllers.
 * Converts exceptions to appropriate HTTP responses with error details.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles validation errors from @Valid annotations.
     * Returns detailed field-level validation error messages.
     * 
     * @param ex The validation exception
     * @param request The web request
     * @return ResponseEntity with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Input validation errors occurred")
                .details(errors)
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Handles JSON parsing errors and enum validation failures.
     * Returns helpful error messages for common enum and parsing issues.
     * 
     * @param ex The JSON parsing exception
     * @param request The web request
     * @return ResponseEntity with helpful error details
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {
        
        String message = "Invalid request data format";
        String error = "Invalid Input";
        
        // Provide specific guidance for enum validation errors
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("CustomerType")) {
                message = "Invalid customer type. Valid values are: PROSPECT, CUSTOMER, PARTNER, VENDOR";
                error = "Invalid Customer Type";
            } else if (ex.getMessage().contains("CustomerStatus")) {
                message = "Invalid customer status. Valid values are: ACTIVE, INACTIVE, SUSPENDED";
                error = "Invalid Customer Status";
            } else if (ex.getMessage().contains("UserRole")) {
                message = "Invalid user role. Valid values are: ADMIN, MANAGER, SALES_REP";
                error = "Invalid User Role";
            } else if (ex.getMessage().contains("LeadStatus")) {
                message = "Invalid lead status. Valid values are: NEW, CONTACTED, QUALIFIED, PROPOSAL, NEGOTIATION, CLOSED_WON, CLOSED_LOST";
                error = "Invalid Lead Status";
            } else if (ex.getMessage().contains("LeadSource")) {
                message = "Invalid lead source. Valid values are: WEBSITE, REFERRAL, SOCIAL_MEDIA, EMAIL, PHONE, TRADE_SHOW, OTHER";
                error = "Invalid Lead Source";
            } else if (ex.getMessage().contains("TaskPriority")) {
                message = "Invalid task priority. Valid values are: LOW, MEDIUM, HIGH, URGENT";
                error = "Invalid Task Priority";
            } else if (ex.getMessage().contains("TaskStatus")) {
                message = "Invalid task status. Valid values are: PENDING, IN_PROGRESS, COMPLETED, CANCELLED";
                error = "Invalid Task Status";
            } else if (ex.getMessage().contains("TaskType")) {
                message = "Invalid task type. Valid values are: FOLLOW_UP, DEMO, PRESENTATION, PROPOSAL, NEGOTIATION, CLOSING, OTHER";
                error = "Invalid Task Type";
            } else if (ex.getMessage().contains("ContactType")) {
                message = "Invalid contact type. Valid values are: PHONE_CALL, EMAIL, MEETING, DEMO, PRESENTATION, FOLLOW_UP, OTHER";
                error = "Invalid Contact Type";
            }
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(error)
                .message(message)
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Handles authentication failures.
     * Returns appropriate error response for bad credentials.
     * 
     * @param ex The authentication exception
     * @param request The web request
     * @return ResponseEntity with authentication error details
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Authentication Failed")
                .message("Invalid username or password")
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    /**
     * Handles user not found exceptions.
     * Returns appropriate error response for missing users.
     * 
     * @param ex The user not found exception
     * @param request The web request
     * @return ResponseEntity with user not found error details
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
            UsernameNotFoundException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("User Not Found")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    /**
     * Handles access denied exceptions.
     * Returns appropriate error response for insufficient permissions.
     * 
     * @param ex The access denied exception
     * @param request The web request
     * @return ResponseEntity with access denied error details
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Access Denied")
                .message("Insufficient permissions to access this resource")
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    
    /**
     * Handles resource not found exceptions.
     * Returns appropriate error response for missing resources.
     * 
     * @param ex The resource not found exception
     * @param request The web request
     * @return ResponseEntity with resource not found error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    /**
     * Handles business logic exceptions.
     * Returns appropriate error response for business rule violations.
     * 
     * @param ex The business exception
     * @param request The web request
     * @return ResponseEntity with business error details
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Business Rule Violation")
                .message(ex.getMessage())
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * Handles generic runtime exceptions.
     * Returns appropriate error response for unexpected errors.
     * 
     * @param ex The runtime exception
     * @param request The web request
     * @return ResponseEntity with generic error details
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    /**
     * Handles all other exceptions not specifically handled.
     * Returns generic error response for unknown errors.
     * 
     * @param ex The exception
     * @param request The web request
     * @return ResponseEntity with generic error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred: " + ex.getMessage())
                .path(request.getDescription(false))
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
