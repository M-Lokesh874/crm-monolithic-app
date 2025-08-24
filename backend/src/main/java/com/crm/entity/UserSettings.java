package com.crm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing user settings and preferences.
 * Stores user-specific configuration for notifications, display, security, and privacy.
 */
@Entity
@Table(name = "user_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @Column(name = "email_notifications", nullable = false)
    private Boolean emailNotifications = true;

    @Column(name = "push_notifications", nullable = false)
    private Boolean pushNotifications = false;

    @Column(name = "sms_notifications", nullable = false)
    private Boolean smsNotifications = false;

    @Column(name = "marketing_notifications", nullable = false)
    private Boolean marketingNotifications = false;

    @Column(name = "system_notifications", nullable = false)
    private Boolean systemNotifications = true;

    @Column(name = "security_notifications", nullable = false)
    private Boolean securityNotifications = true;

    @Column(name = "theme", nullable = false, length = 20)
    private String theme = "light";

    @Column(name = "language", nullable = false, length = 10)
    private String language = "en";

    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone = "UTC";

    @Column(name = "compact_mode", nullable = false)
    private Boolean compactMode = false;

    @Column(name = "show_avatars", nullable = false)
    private Boolean showAvatars = true;

    @Column(name = "show_status", nullable = false)
    private Boolean showStatus = true;

    @Column(name = "two_factor_enabled", nullable = false)
    private Boolean twoFactorEnabled = false;

    @Column(name = "session_timeout", nullable = false)
    private Integer sessionTimeout = 30;

    @Column(name = "require_password_change", nullable = false)
    private Boolean requirePasswordChange = false;

    @Column(name = "login_notifications", nullable = false)
    private Boolean loginNotifications = true;

    @Column(name = "profile_visibility", nullable = false, length = 20)
    private String profileVisibility = "team";

    @Column(name = "activity_visibility", nullable = false, length = 20)
    private String activityVisibility = "team";

    @Column(name = "data_sharing", nullable = false)
    private Boolean dataSharing = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Constructor with user.
     *
     * @param user the user these settings belong to
     */
    public UserSettings(User user) {
        this.user = user;
    }
}
