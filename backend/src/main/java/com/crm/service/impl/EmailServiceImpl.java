package com.crm.service.impl;

import com.crm.entity.User;
import com.crm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of EmailService interface.
 * Provides email functionality using Spring Boot Mail.
 * 
 * @author CRM System
 * @version 1.0
 * @since 2024-01-01
 */
@Service
public class EmailServiceImpl implements EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${crm.app.name:CRM System}")
    private String appName;
    
    @Value("${crm.app.url:http://localhost:3000}")
    private String appUrl;
    
    @Override
    public void sendWelcomeEmail(User user, String plainPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Welcome to " + appName + " - Your Account is Ready!");
        
        String emailContent = buildWelcomeEmailContent(user, plainPassword);
        message.setText(emailContent);
        
        try {
            mailSender.send(message);
            System.out.println("Welcome email sent successfully to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send welcome email to: " + user.getEmail());
            e.printStackTrace();
        }
    }
    
    @Override
    public void sendAdminNotificationEmail(User newUser) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(fromEmail); // Send to admin email
        
        message.setSubject("New User Registration - " + newUser.getUsername());
        
        String emailContent = buildAdminNotificationContent(newUser);
        message.setText(emailContent);
        
        try {
            mailSender.send(message);
            System.out.println("Admin notification email sent successfully");
        } catch (Exception e) {
            System.err.println("Failed to send admin notification email");
            e.printStackTrace();
        }
    }
    
    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request - " + appName);
        
        String emailContent = buildPasswordResetContent(user, resetToken);
        message.setText(emailContent);
        
        try {
            mailSender.send(message);
            System.out.println("Password reset email sent successfully to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send password reset email to: " + user.getEmail());
            e.printStackTrace();
        }
    }
    
    @Override
    public void sendEmailVerificationEmail(User user, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Verify Your Email - " + appName);
        
        String emailContent = buildEmailVerificationContent(user, verificationToken);
        message.setText(emailContent);
        
        try {
            mailSender.send(message);
            System.out.println("Email verification email sent successfully to: " + user.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send email verification email to: " + user.getEmail());
            e.printStackTrace();
        }
    }
    
    private String buildWelcomeEmailContent(User user, String plainPassword) {
        StringBuilder content = new StringBuilder();
        content.append("Hi ").append(user.getFirstName()).append(",\n\n");
        content.append("Welcome to ").append(appName).append("! Your account has been successfully created.\n\n");
        content.append("Login Details:\n");
        content.append("- Username: ").append(user.getUsername()).append("\n");
        content.append("- Password: ").append(plainPassword).append("\n");
        content.append("- Login URL: ").append(appUrl).append("/login\n\n");
        content.append("Your default role: ").append(user.getRole()).append("\n");
        content.append("(Admins can upgrade your role as needed)\n\n");
        content.append("Best regards,\n");
        content.append(appName).append(" Team");
        
        return content.toString();
    }
    
    private String buildAdminNotificationContent(User newUser) {
        StringBuilder content = new StringBuilder();
        content.append("A new user has registered:\n\n");
        content.append("- Name: ").append(newUser.getFirstName()).append(" ").append(newUser.getLastName()).append("\n");
        content.append("- Email: ").append(newUser.getEmail()).append("\n");
        content.append("- Username: ").append(newUser.getUsername()).append("\n");
        content.append("- Role: ").append(newUser.getRole()).append("\n\n");
        content.append("Review and manage users at: ").append(appUrl).append("/admin/users\n\n");
        content.append("Best regards,\n");
        content.append(appName).append(" System");
        
        return content.toString();
    }
    
    private String buildPasswordResetContent(User user, String resetToken) {
        StringBuilder content = new StringBuilder();
        content.append("Hi ").append(user.getFirstName()).append(",\n\n");
        content.append("You have requested a password reset for your ").append(appName).append(" account.\n\n");
        content.append("Reset Token: ").append(resetToken).append("\n\n");
        content.append("Use this token to reset your password at: ").append(appUrl).append("/reset-password\n\n");
        content.append("If you didn't request this, please ignore this email.\n\n");
        content.append("Best regards,\n");
        content.append(appName).append(" Team");
        
        return content.toString();
    }
    
    private String buildEmailVerificationContent(User user, String verificationToken) {
        StringBuilder content = new StringBuilder();
        content.append("Hi ").append(user.getFirstName()).append(",\n\n");
        content.append("Please verify your email address for your ").append(appName).append(" account.\n\n");
        content.append("Verification Token: ").append(verificationToken).append("\n\n");
        content.append("Use this token to verify your email at: ").append(appUrl).append("/verify-email\n\n");
        content.append("This token expires in 24 hours.\n\n");
        content.append("Best regards,\n");
        content.append(appName).append(" Team");
        
        return content.toString();
    }
}
