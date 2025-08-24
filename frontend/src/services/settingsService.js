import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

/**
 * Settings Service - Handles user preferences and system settings
 */
class SettingsService {
  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add auth token interceptor
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );
  }

  /**
   * Get user settings and preferences
   * @returns {Promise<Object>} User settings
   */
  async getUserSettings() {
    try {
      const response = await this.api.get('/settings/user');
      return response.data;
    } catch (error) {
      console.error('Error fetching user settings:', error);
      // Return default settings if API fails
      return this.getDefaultSettings();
    }
  }

  /**
   * Update user settings
   * @param {Object} settings - Settings to update
   * @returns {Promise<Object>} Updated settings
   */
  async updateUserSettings(settings) {
    try {
      const response = await this.api.put('/settings/user', settings);
      return response.data;
    } catch (error) {
      console.error('Error updating user settings:', error);
      throw error;
    }
  }

  /**
   * Update user password
   * @param {Object} passwordData - Password update data
   * @returns {Promise<Object>} Success response
   */
  async updatePassword(passwordData) {
    try {
      const response = await this.api.put('/settings/password', passwordData);
      return response.data;
    } catch (error) {
      console.error('Error updating password:', error);
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw error;
    }
  }

  /**
   * Get system settings (admin only)
   * @returns {Promise<Object>} System settings
   */
  async getSystemSettings() {
    try {
      const response = await this.api.get('/settings/system');
      return response.data;
    } catch (error) {
      console.error('Error fetching system settings:', error);
      throw error;
    }
  }

  /**
   * Update system settings (admin only)
   * @param {Object} settings - System settings to update
   * @returns {Promise<Object>} Updated system settings
   */
  async updateSystemSettings(settings) {
    try {
      const response = await this.api.put('/settings/system', settings);
      return response.data;
    } catch (error) {
      console.error('Error updating system settings:', error);
      throw error;
    }
  }

  /**
   * Get available themes
   * @returns {Promise<Array>} Available themes
   */
  async getAvailableThemes() {
    try {
      const response = await this.api.get('/settings/themes');
      return response.data;
    } catch (error) {
      console.error('Error fetching themes:', error);
      // Return default themes if API fails
      return [
        { id: 'light', name: 'Light', description: 'Light theme for daytime use' },
        { id: 'dark', name: 'Dark', description: 'Dark theme for low-light environments' },
        { id: 'auto', name: 'Auto', description: 'Automatically switch based on system preference' }
      ];
    }
  }

  /**
   * Get available languages
   * @returns {Promise<Array>} Available languages
   */
  async getAvailableLanguages() {
    try {
      const response = await this.api.get('/settings/languages');
      return response.data;
    } catch (error) {
      console.error('Error fetching languages:', error);
      // Return default languages if API fails
      return [
        { code: 'en', name: 'English', nativeName: 'English' },
        { code: 'es', name: 'Spanish', nativeName: 'Español' },
        { code: 'fr', name: 'French', nativeName: 'Français' },
        { code: 'de', name: 'German', nativeName: 'Deutsch' }
      ];
    }
  }

  /**
   * Get available timezones
   * @returns {Promise<Array>} Available timezones
   */
  async getAvailableTimezones() {
    try {
      const response = await this.api.get('/settings/timezones');
      return response.data;
    } catch (error) {
      console.error('Error fetching timezones:', error);
      // Return default timezones if API fails
      return [
        { id: 'UTC', name: 'UTC', offset: '+00:00' },
        { id: 'EST', name: 'Eastern Time', offset: '-05:00' },
        { id: 'PST', name: 'Pacific Time', offset: '-08:00' },
        { id: 'GMT', name: 'Greenwich Mean Time', offset: '+00:00' }
      ];
    }
  }

  /**
   * Get default settings for new users
   * @returns {Object} Default settings
   */
  getDefaultSettings() {
    return {
      notifications: {
        email: true,
        push: false,
        sms: false,
        marketing: false,
        system: true,
        security: true
      },
      display: {
        theme: 'light',
        language: 'en',
        timezone: 'UTC',
        compactMode: false,
        showAvatars: true,
        showStatus: true
      },
      security: {
        twoFactor: false,
        sessionTimeout: 30,
        requirePasswordChange: false,
        loginNotifications: true
      },
      privacy: {
        profileVisibility: 'team',
        activityVisibility: 'team',
        dataSharing: false
      }
    };
  }

  /**
   * Export user settings
   * @returns {Promise<Blob>} Settings file as blob
   */
  async exportSettings() {
    try {
      const settings = await this.getUserSettings();
      const blob = new Blob([JSON.stringify(settings, null, 2)], {
        type: 'application/json'
      });
      return blob;
    } catch (error) {
      console.error('Error exporting settings:', error);
      throw error;
    }
  }

  /**
   * Import user settings
   * @param {File} file - Settings file to import
   * @returns {Promise<Object>} Imported settings
   */
  async importSettings(file) {
    try {
      const text = await file.text();
      const settings = JSON.parse(text);
      
      // Validate settings structure
      if (!this.validateSettings(settings)) {
        throw new Error('Invalid settings file format');
      }
      
      // Update settings
      return await this.updateUserSettings(settings);
    } catch (error) {
      console.error('Error importing settings:', error);
      throw error;
    }
  }

  /**
   * Validate settings structure
   * @param {Object} settings - Settings to validate
   * @returns {boolean} True if valid
   */
  validateSettings(settings) {
    const requiredSections = ['notifications', 'display', 'security', 'privacy'];
    return requiredSections.every(section => 
      settings[section] && typeof settings[section] === 'object'
    );
  }

  /**
   * Reset user settings to defaults
   * @returns {Promise<Object>} Reset settings
   */
  async resetToDefaults() {
    try {
      const defaultSettings = this.getDefaultSettings();
      return await this.updateUserSettings(defaultSettings);
    } catch (error) {
      console.error('Error resetting settings:', error);
      throw error;
    }
  }
}

export default new SettingsService();
