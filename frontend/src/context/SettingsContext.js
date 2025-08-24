import React, { createContext, useContext, useState, useEffect } from 'react';
import settingsService from '../services/settingsService';

const SettingsContext = createContext();

export const useSettings = () => {
  const context = useContext(SettingsContext);
  if (!context) {
    throw new Error('useSettings must be used within a SettingsProvider');
  }
  return context;
};

export const SettingsProvider = ({ children }) => {
  const [settings, setSettings] = useState({
    notifications: {
      email: true,
      push: true,
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
      twoFactorEnabled: false,
      sessionTimeout: 30,
      requirePasswordChange: false,
      loginNotifications: true
    },
    privacy: {
      profileVisibility: 'public',
      activityVisibility: 'team',
      dataSharing: 'minimal'
    }
  });

  const [loading, setLoading] = useState(true);
  const [availableThemes, setAvailableThemes] = useState([]);
  const [availableLanguages, setAvailableLanguages] = useState([]);
  const [availableTimezones, setAvailableTimezones] = useState([]);

  // Load settings on mount
  useEffect(() => {
    loadSettings();
  }, []);

  // Always apply light theme for consistency
  useEffect(() => {
    applyTheme('light');
  }, []);

  // Load all settings data
  const loadSettings = async () => {
    try {
      setLoading(true);
      
      // Load user settings
      const userSettings = await settingsService.getUserSettings();
      if (userSettings) {
        setSettings(prevSettings => ({
          ...prevSettings,
          ...userSettings
        }));
      }

      // Load available options
      const [themes, languages, timezones] = await Promise.all([
        settingsService.getAvailableThemes(),
        settingsService.getAvailableLanguages(),
        settingsService.getAvailableTimezones()
      ]);

      setAvailableThemes(themes);
      setAvailableLanguages(languages);
      setAvailableTimezones(timezones);
    } catch (error) {
      console.error('Error loading settings:', error);
    } finally {
      setLoading(false);
    }
  };

  // Update a specific setting
  const updateSetting = async (category, key, value) => {
    try {
      const updatedSettings = {
        ...settings,
        [category]: {
          ...settings[category],
          [key]: value
        }
      };

      // Update local state immediately for responsive UI
      setSettings(updatedSettings);

      // Update backend
      await settingsService.updateUserSettings(updatedSettings);

      // Prevent theme changes for consistency
      if (category === 'display' && key === 'theme') {
        console.log('Theme changes are disabled for consistent user experience');
        return { success: false, error: 'Theme changes are disabled for consistent user experience' };
      }

      return { success: true };
    } catch (error) {
      console.error('Error updating setting:', error);
      // Revert local state on error
      setSettings(settings);
      return { success: false, error: error.message };
    }
  };

  // Update multiple settings at once
  const updateSettings = async (newSettings) => {
    try {
      const updatedSettings = { ...settings, ...newSettings };
      
      // Update local state
      setSettings(updatedSettings);

      // Update backend
      await settingsService.updateUserSettings(updatedSettings);

      // Prevent theme changes for consistency
      if (newSettings.display?.theme) {
        console.log('Theme changes are disabled for consistent user experience');
        return { success: false, error: 'Theme changes are disabled for consistent user experience' };
      }

      return { success: true };
    } catch (error) {
      console.error('Error updating settings:', error);
      // Revert local state on error
      setSettings(settings);
      return { success: false, error: error.message };
    }
  };

  // Apply theme to the document
  const applyTheme = (theme) => {
    const root = document.documentElement;
    const body = document.body;
    
    // Remove existing theme classes
    root.classList.remove('theme-light', 'theme-dark');
    body.classList.remove('theme-light', 'theme-dark');
    
    // Add new theme class to both root and body
    root.classList.add(`theme-${theme}`);
    body.classList.add(`theme-${theme}`);
    
    // Set CSS custom properties for theme colors
    if (theme === 'dark') {
      root.style.setProperty('--bg-primary', '#1a1a1a');
      root.style.setProperty('--bg-secondary', '#2d2d2d');
      root.style.setProperty('--text-primary', '#ffffff');
      root.style.setProperty('--text-secondary', '#b3b3b3');
      root.style.setProperty('--border-color', '#404040');
      root.style.setProperty('--accent-color', '#3b82f6');
      
      // Apply dark theme to body
      body.style.backgroundColor = '#1a1a1a';
      body.style.color = '#ffffff';
    } else {
      root.style.setProperty('--bg-primary', '#ffffff');
      root.style.setProperty('--bg-secondary', '#f8fafc');
      root.style.setProperty('--text-primary', '#1e293b');
      root.style.setProperty('--text-secondary', '#64748b');
      root.style.setProperty('--border-color', '#e2e8f0');
      root.style.setProperty('--accent-color', '#3b82f6');
      
      // Apply light theme to body
      body.style.backgroundColor = '#ffffff';
      body.style.color = '#1e293b';
    }
    
    // Force a re-render by triggering a resize event
    window.dispatchEvent(new Event('resize'));
  };

  // Reset settings to defaults
  const resetToDefaults = async () => {
    try {
      const defaultSettings = await settingsService.getDefaultSettings();
      await updateSettings(defaultSettings);
      return { success: true };
    } catch (error) {
      console.error('Error resetting settings:', error);
      return { success: false, error: error.message };
    }
  };

  // Export settings
  const exportSettings = async () => {
    try {
      const exportedData = await settingsService.exportSettings();
      const blob = new Blob([JSON.stringify(exportedData, null, 2)], {
        type: 'application/json'
      });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'crm-settings.json';
      a.click();
      URL.revokeObjectURL(url);
      return { success: true };
    } catch (error) {
      console.error('Error exporting settings:', error);
      return { success: false, error: error.message };
    }
  };

  // Import settings
  const importSettings = async (file) => {
    try {
      const text = await file.text();
      const importedSettings = JSON.parse(text);
      
      // Validate imported settings
      const validationResult = await settingsService.validateSettings(importedSettings);
      if (!validationResult.valid) {
        throw new Error(validationResult.errors.join(', '));
      }

      // Apply imported settings
      await updateSettings(importedSettings);
      return { success: true };
    } catch (error) {
      console.error('Error importing settings:', error);
      return { success: false, error: error.message };
    }
  };

  // Update password
  const updatePassword = async (passwordData) => {
    try {
      await settingsService.updatePassword(passwordData);
      return { success: true };
    } catch (error) {
      console.error('Error updating password:', error);
      return { success: false, error: error.message };
    }
  };

  const value = {
    settings,
    loading,
    availableThemes,
    availableLanguages,
    availableTimezones,
    updateSetting,
    updateSettings,
    updatePassword,
    resetToDefaults,
    exportSettings,
    importSettings,
    loadSettings
  };

  return (
    <SettingsContext.Provider value={value}>
      {children}
    </SettingsContext.Provider>
  );
};
