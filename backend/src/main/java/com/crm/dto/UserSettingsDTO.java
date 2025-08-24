package com.crm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user settings and preferences.
 * Used for transferring user settings data between frontend and backend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private NotificationSettingsDTO notifications;
    private DisplaySettingsDTO display;
    private SecuritySettingsDTO security;
    private PrivacySettingsDTO privacy;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationSettingsDTO {
        private Boolean email = true;
        private Boolean push = false;
        private Boolean sms = false;
        private Boolean marketing = false;
        private Boolean system = true;
        private Boolean security = true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisplaySettingsDTO {
        private String theme = "light";
        private String language = "en";
        private String timezone = "UTC";
        private Boolean compactMode = false;
        private Boolean showAvatars = true;
        private Boolean showStatus = true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SecuritySettingsDTO {
        private Boolean twoFactor = false;
        private Integer sessionTimeout = 30;
        private Boolean requirePasswordChange = false;
        private Boolean loginNotifications = true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrivacySettingsDTO {
        private String profileVisibility = "team";
        private String activityVisibility = "team";
        private Boolean dataSharing = false;
    }
}
