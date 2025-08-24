package com.crm.service;

import com.crm.dto.UserSettingsDTO;

/**
 * Service interface for user settings operations.
 * Provides methods for managing user preferences and settings.
 */
public interface UserSettingsService {

    /**
     * Get user settings by user ID.
     *
     * @param userId the user ID
     * @return user settings DTO
     */
    UserSettingsDTO getUserSettings(Long userId);

    /**
     * Update user settings.
     *
     * @param userId the user ID
     * @param settingsDTO the settings to update
     * @return updated user settings DTO
     */
    UserSettingsDTO updateUserSettings(Long userId, UserSettingsDTO settingsDTO);

    /**
     * Create default settings for a new user.
     *
     * @param userId the user ID
     * @return created user settings DTO
     */
    UserSettingsDTO createDefaultSettings(Long userId);

    /**
     * Delete user settings.
     *
     * @param userId the user ID
     */
    void deleteUserSettings(Long userId);
}
