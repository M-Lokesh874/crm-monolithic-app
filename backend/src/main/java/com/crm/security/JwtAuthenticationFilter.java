package com.crm.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter for Spring Security.
 * 
 * This filter intercepts incoming HTTP requests and processes JWT tokens
 * for authentication. It extracts JWT tokens from the Authorization header,
 * validates them, and sets the authentication context for the request.
 * 
 * The filter operates as part of the Spring Security filter chain and:
 * <ul>
 *   <li>Intercepts all incoming requests</li>
 *   <li>Extracts JWT tokens from Authorization headers</li>
 *   <li>Validates token authenticity and expiration</li>
 *   <li>Loads user details for authenticated tokens</li>
 *   <li>Sets Spring Security context for authenticated requests</li>
 *   <li>Allows unauthenticated requests to pass through</li>
 * </ul>
 * 
 * This filter is essential for stateless JWT-based authentication in the
 * CRM system, eliminating the need for server-side session storage.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see org.springframework.web.filter.OncePerRequestFilter
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see com.crm.security.JwtUtil
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    /**
     * Service for loading user details from the database.
     * Used to retrieve user information after JWT validation.
     */
    private final UserDetailsService userDetailsService;
    
    /**
     * Utility class for JWT operations.
     * Used to extract and validate JWT tokens.
     */
    private final JwtUtil jwtUtil;
    
    /**
     * Constructor for JWT Authentication Filter.
     * 
     * @param userDetailsService Service for loading user details
     * @param jwtUtil Utility for JWT operations
     */
    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * Processes each HTTP request to check for JWT authentication.
     * 
     * This method is called for every incoming request and performs the
     * following steps:
     * <ol>
     *   <li>Extracts the JWT token from the Authorization header</li>
     *   <li>Validates the token using JwtUtil</li>
     *   <li>Loads user details if token is valid</li>
     *   <li>Sets the authentication context for the request</li>
     *   <li>Continues the filter chain</li>
     * </ol>
     * 
     * If no token is present or the token is invalid, the request
     * continues without authentication context.
     * 
     * @param request The HTTP request to process
     * @param response The HTTP response (not modified by this filter)
     * @param filterChain The filter chain to continue processing
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) 
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;
        
        // Extract JWT token from Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token extraction failed, continue without authentication
                logger.warn("Failed to extract username from JWT token: " + e.getMessage());
            }
        }
        
        // Set authentication context if valid token found
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
