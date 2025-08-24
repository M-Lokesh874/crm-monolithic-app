package com.crm.service;

import com.crm.entity.User;

/**
 * Service interface for email operations.
 * Provides methods for sending various types of email notifications.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
public interface EmailService {
    
    /**
     * Sends a welcome email to newly registered users.
     * 
     * @param user The user who just registered
     * @param plainPassword The plain text password for the user
     */
    void sendWelcomeEmail(User user, String plainPassword);
    
    /**
     * Sends an admin notification about a new user registration.
     * 
     * @param newUser The newly registered user
     */
    void sendAdminNotificationEmail(User newUser);
    
    /**
     * Sends a password reset email to a user.
     * 
     * @param user The user requesting password reset
     * @param resetToken The password reset token
     */
    void sendPasswordResetEmail(User user, String resetToken);
    
    /**
     * Sends an email verification email to a user.
     * 
     * @param user The user to verify
     * @param verificationToken The verification token
     */
    void sendEmailVerificationEmail(User user, String verificationToken);
}
