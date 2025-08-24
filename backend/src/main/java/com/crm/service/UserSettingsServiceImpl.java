package com.crm.service;

import com.crm.dto.UserSettingsDTO;
import com.crm.entity.User;
import com.crm.entity.UserSettings;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.UserRepository;
import com.crm.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for user settings operations.
 * Handles business logic for managing user preferences and settings.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;

    @Override
    public UserSettingsDTO getUserSettings(Long userId) {
        log.info("Fetching user settings for user ID: {}", userId);
        
        UserSettings userSettings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettingsEntity(userId));
        
        return mapToDTO(userSettings);
    }

    @Override
    public UserSettingsDTO updateUserSettings(Long userId, UserSettingsDTO settingsDTO) {
        log.info("Updating user settings for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        UserSettings userSettings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> new UserSettings(user));
        
        // Update notification settings
        if (settingsDTO.getNotifications() != null) {
            UserSettingsDTO.NotificationSettingsDTO notifications = settingsDTO.getNotifications();
            userSettings.setEmailNotifications(notifications.getEmail());
            userSettings.setPushNotifications(notifications.getPush());
            userSettings.setSmsNotifications(notifications.getSms());
            userSettings.setMarketingNotifications(notifications.getMarketing());
            userSettings.setSystemNotifications(notifications.getSystem());
            userSettings.setSecurityNotifications(notifications.getSecurity());
        }
        
        // Update display settings
        if (settingsDTO.getDisplay() != null) {
            UserSettingsDTO.DisplaySettingsDTO display = settingsDTO.getDisplay();
            userSettings.setTheme(display.getTheme());
            userSettings.setLanguage(display.getLanguage());
            userSettings.setTimezone(display.getTimezone());
            userSettings.setCompactMode(display.getCompactMode());
            userSettings.setShowAvatars(display.getShowAvatars());
            userSettings.setShowStatus(display.getShowStatus());
        }
        
        // Update security settings
        if (settingsDTO.getSecurity() != null) {
            UserSettingsDTO.SecuritySettingsDTO security = settingsDTO.getSecurity();
            userSettings.setTwoFactorEnabled(security.getTwoFactor());
            userSettings.setSessionTimeout(security.getSessionTimeout());
            userSettings.setRequirePasswordChange(security.getRequirePasswordChange());
            userSettings.setLoginNotifications(security.getLoginNotifications());
        }
        
        // Update privacy settings
        if (settingsDTO.getPrivacy() != null) {
            UserSettingsDTO.PrivacySettingsDTO privacy = settingsDTO.getPrivacy();
            userSettings.setProfileVisibility(privacy.getProfileVisibility());
            userSettings.setActivityVisibility(privacy.getActivityVisibility());
            userSettings.setDataSharing(privacy.getDataSharing());
        }
        
        UserSettings savedSettings = userSettingsRepository.save(userSettings);
        return mapToDTO(savedSettings);
    }

    @Override
    public UserSettingsDTO createDefaultSettings(Long userId) {
        log.info("Creating default settings for user ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        UserSettings defaultSettings = new UserSettings(user);
        UserSettings savedSettings = userSettingsRepository.save(defaultSettings);
        
        return mapToDTO(savedSettings);
    }

    @Override
    public void deleteUserSettings(Long userId) {
        log.info("Deleting user settings for user ID: {}", userId);
        
        UserSettings userSettings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User settings not found for user ID: " + userId));
        
        userSettingsRepository.delete(userSettings);
    }

    /**
     * Create default settings entity for a new user.
     *
     * @param userId the user ID
     * @return default user settings entity
     */
    private UserSettings createDefaultSettingsEntity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        
        UserSettings defaultSettings = new UserSettings(user);
        return userSettingsRepository.save(defaultSettings);
    }

    /**
     * Map UserSettings entity to UserSettingsDTO.
     *
     * @param userSettings the user settings entity
     * @return user settings DTO
     */
    private UserSettingsDTO mapToDTO(UserSettings userSettings) {
        UserSettingsDTO dto = new UserSettingsDTO();
        dto.setId(userSettings.getId());
        
        // Map notification settings
        UserSettingsDTO.NotificationSettingsDTO notifications = new UserSettingsDTO.NotificationSettingsDTO();
        notifications.setEmail(userSettings.getEmailNotifications());
        notifications.setPush(userSettings.getPushNotifications());
        notifications.setSms(userSettings.getSmsNotifications());
        notifications.setMarketing(userSettings.getMarketingNotifications());
        notifications.setSystem(userSettings.getSystemNotifications());
        notifications.setSecurity(userSettings.getSecurityNotifications());
        dto.setNotifications(notifications);
        
        // Map display settings
        UserSettingsDTO.DisplaySettingsDTO display = new UserSettingsDTO.DisplaySettingsDTO();
        display.setTheme(userSettings.getTheme());
        display.setLanguage(userSettings.getLanguage());
        display.setTimezone(userSettings.getTimezone());
        display.setCompactMode(userSettings.getCompactMode());
        display.setShowAvatars(userSettings.getShowAvatars());
        display.setShowStatus(userSettings.getShowStatus());
        dto.setDisplay(display);
        
        // Map security settings
        UserSettingsDTO.SecuritySettingsDTO security = new UserSettingsDTO.SecuritySettingsDTO();
        security.setTwoFactor(userSettings.getTwoFactorEnabled());
        security.setSessionTimeout(userSettings.getSessionTimeout());
        security.setRequirePasswordChange(userSettings.getRequirePasswordChange());
        security.setLoginNotifications(userSettings.getLoginNotifications());
        dto.setSecurity(security);
        
        // Map privacy settings
        UserSettingsDTO.PrivacySettingsDTO privacy = new UserSettingsDTO.PrivacySettingsDTO();
        privacy.setProfileVisibility(userSettings.getProfileVisibility());
        privacy.setActivityVisibility(userSettings.getActivityVisibility());
        privacy.setDataSharing(userSettings.getDataSharing());
        dto.setPrivacy(privacy);
        
        return dto;
    }
}
