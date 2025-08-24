package com.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for the CRM system.
 * 
 * This class serves as the entry point for the CRM application and provides
 * the following functionality:
 * <ul>
 *   <li>Spring Boot auto-configuration</li>
 *   <li>Component scanning for the com.crm package</li>
 *   <li>Scheduling capabilities for background tasks</li>
 * </ul>
 * 
 * The application follows a layered architecture pattern with:
 * <ul>
 *   <li>Presentation Layer: REST Controllers</li>
 *   <li>Business Layer: Service interfaces and implementations</li>
 *   <li>Data Access Layer: Repository interfaces</li>
 *   <li>Data Layer: JPA Entities</li>
 * </ul>
 * 
 * Key features include:
 * <ul>
 *   <li>JWT-based authentication and authorization</li>
 *   <li>Role-based access control (RBAC)</li>
 *   <li>PostgreSQL database integration</li>
 *   <li>RESTful API endpoints</li>
 *   <li>Global exception handling</li>
 *   <li>Comprehensive logging and monitoring</li>
 * </ul>
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 * @see org.springframework.boot.SpringBootApplication
 * @see org.springframework.scheduling.annotation.EnableScheduling
 */
@SpringBootApplication
@EnableScheduling
public class CrmApplication {
    
    /**
     * Main method that starts the Spring Boot application.
     * 
     * This method initializes the Spring application context, starts the embedded
     * web server, and begins listening for HTTP requests. The application will
     * continue running until explicitly stopped.
     * 
     * @param args Command line arguments passed to the application
     * @throws Exception if the application fails to start
     */
    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}
