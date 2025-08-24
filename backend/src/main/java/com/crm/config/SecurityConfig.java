package com.crm.config;

import com.crm.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import com.crm.security.JwtUtil;

/**
 * Spring Security configuration for the CRM application.
 * 
 * This configuration class sets up the security framework for the CRM system,
 * including JWT-based authentication, CORS configuration, and method-level
 * security. It defines which endpoints are publicly accessible and which
 * require authentication and authorization.
 * 
 * Key security features include:
 * <ul>
 *   <li>JWT-based stateless authentication</li>
 *   <li>Role-based access control (RBAC)</li>
 *   <li>CORS configuration for cross-origin requests</li>
 *   <li>Password encryption using BCrypt</li>
 *   <li>Method-level security with @PreAuthorize annotations</li>
 *   <li>Session management configuration</li>
 * </ul>
 * 
 * The configuration ensures that:
 * <ul>
 *   <li>Authentication endpoints are publicly accessible</li>
 *   <li>Protected endpoints require valid JWT tokens</li>
 *   <li>User roles determine access to specific resources</li>
 *   <li>Cross-origin requests are properly handled</li>
 * </ul>
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 * @see org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
 * @see com.crm.security.JwtAuthenticationFilter
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    /**
     * Utility class for JWT operations.
     * Used to extract and validate JWT tokens.
     */
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Application context for accessing beans.
     * Used to resolve circular dependency issues.
     */
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * Creates and configures the JWT authentication filter.
     * 
     * This bean method creates the JWT filter with its required dependencies,
     * avoiding circular dependency issues that would occur with field injection.
     * 
     * @param userDetailsService The UserDetailsService bean from Spring context
     * @return The configured JWT authentication filter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(userDetailsService, jwtUtil);
    }
    
    /**
     * Configures the security filter chain for HTTP requests.
     * 
     * This method sets up the complete security configuration including:
     * <ul>
     *   <li>Public endpoints that don't require authentication</li>
     *   <li>Protected endpoints that require valid JWT tokens</li>
     *   <li>CORS configuration for cross-origin requests</li>
     *   <li>Session management (stateless for JWT)</li>
     *   <li>CSRF protection (disabled for JWT)</li>
     *   <li>JWT filter integration</li>
     * </ul>
     * 
     * @param http The HttpSecurity object to configure
     * @return The configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll()
                .requestMatchers("/customers/enums", "/customers/system-enums").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(applicationContext.getBean(JwtAuthenticationFilter.class), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * Configures the authentication manager for user authentication.
     * 
     * This bean is required for the authentication service to validate
     * user credentials during login operations.
     * 
     * @param authConfig The authentication configuration
     * @return The configured AuthenticationManager
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * Configures the password encoder for secure password storage.
     * 
     * This bean provides BCrypt password encoding for secure storage
     * of user passwords in the database. BCrypt is a strong hashing
     * algorithm that includes salt and is resistant to rainbow table attacks.
     * 
     * @return The configured PasswordEncoder (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Configures CORS (Cross-Origin Resource Sharing) for the application.
     * 
     * This configuration allows the frontend application to make requests
     * to the backend API from different origins (domains, ports, protocols).
     * It's essential for development and production deployments where
     * frontend and backend may be hosted on different domains.
     * 
     * The configuration allows:
     * <ul>
     *   <li>All origins (configurable for production)</li>
     *   <li>Common HTTP methods (GET, POST, PUT, PATCH, DELETE, OPTIONS)</li>
     *   <li>Common headers including Authorization for JWT tokens</li>
     *   <li>Credentials for authenticated requests</li>
     * </ul>
     * 
     * @return The configured CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
