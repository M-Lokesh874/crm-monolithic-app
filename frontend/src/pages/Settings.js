import React, { useState, useEffect } from 'react';
import { 
  Settings as SettingsIcon, 
  User, 
  Bell, 
  Shield, 
  Palette,
  Database,
  Globe,
  Save,
  Download,
  Upload,
  RotateCcw,
  Lock,
  Smartphone,
  Mail,
  Monitor,
  Sun,
  Moon,
  Zap,
  Eye
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useSettings } from '../context/SettingsContext';
import PageHeader from '../components/PageHeader';
import toast from 'react-hot-toast';


const Settings = () => {
  const { user } = useAuth();
  const { 
    settings, 
    loading, 
    availableThemes, 
    availableLanguages, 
    availableTimezones,
    updateSetting,
    updateSettings,
    resetToDefaults,
    exportSettings,
    importSettings
  } = useSettings();
  
  const [saving, setSaving] = useState(false);



  const handleSettingChange = async (category, key, value) => {
    try {
      const result = await updateSetting(category, key, value);
      if (result.success) {
        toast.success('Setting updated successfully');
      } else {
        toast.error(result.error || 'Failed to update setting');
      }
    } catch (error) {
      toast.error('Failed to update setting');
    }
  };



  const handleExportSettings = async () => {
    try {
      const result = await exportSettings();
      if (result.success) {
        toast.success('Settings exported successfully');
      } else {
        toast.error(result.error || 'Failed to export settings');
      }
    } catch (error) {
      toast.error('Failed to export settings');
    }
  };

  const handleImportSettings = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    try {
      const result = await importSettings(file);
      if (result.success) {
        toast.success('Settings imported successfully');
      } else {
        toast.error(result.error || 'Failed to import settings');
      }
    } catch (error) {
      toast.error('Failed to import settings');
    }
    
    // Reset file input
    event.target.value = '';
  };

  const handleResetToDefaults = async () => {
    if (window.confirm('Are you sure you want to reset all settings to defaults? This action cannot be undone.')) {
      try {
        const result = await resetToDefaults();
        if (result.success) {
          toast.success('Settings reset to defaults');
        } else {
          toast.error(result.error || 'Failed to reset settings');
        }
      } catch (error) {
        toast.error('Failed to reset settings');
      }
    }
  };

  const SettingSection = ({ title, icon: Icon, children, className = '' }) => (
    <div className={`p-6 rounded-lg shadow border transition-colors ${className}`} style={{ 
      backgroundColor: 'var(--bg-primary)',
      borderColor: 'var(--border-color)'
    }}>
      <div className="flex items-center mb-4">
        <Icon className="h-5 w-5 text-primary-600 mr-2" />
        <h3 className="text-lg font-medium" style={{ color: 'var(--text-primary)' }}>{title}</h3>
      </div>
      {children}
    </div>
  );

  const SettingItem = ({ label, description, children, className = '' }) => (
    <div className={`flex items-center justify-between py-3 border-b last:border-b-0 transition-colors ${className}`} style={{ borderColor: 'var(--border-color)' }}>
      <div className="flex-1">
        <div className="text-sm font-medium" style={{ color: 'var(--text-primary)' }}>{label}</div>
        {description && (
          <div className="text-sm" style={{ color: 'var(--text-secondary)' }}>{description}</div>
        )}
      </div>
      <div className="ml-4">
        {children}
      </div>
    </div>
  );

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <PageHeader
        title="Settings & Configuration"
        subtitle="Manage your account preferences and system settings"
        showSearch={false}
        showFilters={false}
      />

      {/* Profile Settings */}
      <SettingSection title="Profile Settings" icon={User}>
        <div className="space-y-4">
          <SettingItem
            label="Display Name"
            description="How your name appears to other users"
          >
            <input
              type="text"
              value={user?.firstName && user?.lastName ? `${user.firstName} ${user.lastName}` : user?.username || 'N/A'}
              disabled
              className="px-3 py-2 border rounded-md transition-colors"
              style={{ 
                borderColor: 'var(--border-color)',
                backgroundColor: 'var(--bg-secondary)',
                color: 'var(--text-secondary)',
                height: '42px'
              }}
            />
          </SettingItem>
          
          <SettingItem
            label="Username"
            description="Your unique username for login"
          >
            <input
              type="text"
              value={user?.username || 'N/A'}
              disabled
              className="px-3 py-2 border rounded-md transition-colors"
              style={{ 
                borderColor: 'var(--border-color)',
                backgroundColor: 'var(--bg-secondary)',
                color: 'var(--text-secondary)',
                height: '42px'
              }}
            />
          </SettingItem>
          
          <SettingItem
            label="Email Address"
            description="Your primary contact email"
          >
            <input
              type="email"
              value={user?.email || 'N/A'}
              disabled
              className="px-3 py-2 border rounded-md transition-colors"
              style={{ 
                borderColor: 'var(--border-color)',
                backgroundColor: 'var(--bg-secondary)',
                color: 'var(--text-secondary)',
                height: '42px'
              }}
            />
          </SettingItem>
          
          <SettingItem
            label="User Role"
            description="Your current role and permissions"
          >
            <span className="inline-flex px-3 py-1 text-sm font-medium rounded-full bg-primary-100 text-primary-800">
              {user?.role || 'N/A'}
            </span>
          </SettingItem>
        </div>
      </SettingSection>

             {/* Password Settings */}
       <SettingSection title="Password & Security" icon={Lock}>
         <div className="space-y-4">
           <SettingItem
             label="Current Password"
             description="Password change functionality is disabled for security"
           >
             <input
               type="password"
               value="••••••••"
               disabled
               className="px-3 py-2 border rounded-md transition-colors"
               style={{ 
                 borderColor: 'var(--border-color)',
                 backgroundColor: 'var(--bg-secondary)',
                 color: 'var(--text-secondary)',
                 height: '42px'
               }}
             />
           </SettingItem>
           
           <SettingItem
             label="New Password"
             description="Password change functionality is disabled for security"
           >
             <input
               type="password"
               value="••••••••"
               disabled
               className="px-3 py-2 border rounded-md transition-colors"
               style={{ 
                 borderColor: 'var(--border-color)',
                 backgroundColor: 'var(--bg-secondary)',
                 color: 'var(--text-secondary)',
                 height: '42px'
               }}
             />
           </SettingItem>
           
           <SettingItem
             label="Confirm New Password"
             description="Password change functionality is disabled for security"
             
           >
             <input
               type="password"
               value="••••••••"
               disabled
               className="px-3 py-2 border rounded-md transition-colors"
               style={{ 
                 borderColor: 'var(--border-color)',
                 backgroundColor: 'var(--bg-secondary)',
                 color: 'var(--text-secondary)',
                 height: '42px'
               }}
             />
           </SettingItem>
           
           <div className="pt-3">
             <div className="text-sm text-yellow-600 bg-yellow-50 p-3 rounded-md border border-yellow-200">
               <strong>Note:</strong> Password change functionality is currently disabled for security purposes. 
               Please contact your system administrator if you need to change your password.
             </div>
           </div>
         </div>
       </SettingSection>

      {/* Notification Preferences */}
      <SettingSection title="Notification Preferences" icon={Bell}>
        <div className="space-y-4">
          <SettingItem
            label="Email Notifications"
            description="Receive notifications via email"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.notifications.email}
                onChange={(e) => handleSettingChange('notifications', 'email', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
          
          <SettingItem
            label="Push Notifications"
            description="Receive push notifications in browser"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.notifications.push}
                onChange={(e) => handleSettingChange('notifications', 'push', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
          
          <SettingItem
            label="SMS Notifications"
            description="Receive notifications via SMS"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.notifications.sms}
                onChange={(e) => handleSettingChange('notifications', 'sms', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
          
          <SettingItem
            label="Marketing Notifications"
            description="Receive promotional and marketing content"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.notifications.marketing}
                onChange={(e) => handleSettingChange('notifications', 'marketing', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
          
          <SettingItem
            label="System Notifications"
            description="Receive system updates and maintenance notices"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.notifications.system}
                onChange={(e) => handleSettingChange('notifications', 'system', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
        </div>
      </SettingSection>

      {/* Display Settings */}
      <SettingSection title="Display Settings" icon={Palette}>
        <div className="space-y-4">
                     <SettingItem
             label="Theme"
             description="Theme selection is disabled for consistent user experience"
           >
             <select
               value="light"
               disabled
               className="px-3 py-2 border rounded-md transition-colors"
               style={{ 
                 borderColor: 'var(--border-color)',
                 backgroundColor: 'var(--bg-secondary)',
                 color: 'var(--text-secondary)'
               }}
             >
               <option value="light">Light Theme (Default)</option>
             </select>
           </SettingItem>
           
           <div className="pt-3">
             <div className="text-sm text-blue-600 bg-blue-50 p-3 rounded-md border border-blue-200">
               <strong>Note:</strong> Theme selection is currently disabled to ensure a consistent and professional user experience across all devices and browsers.
             </div>
           </div>
          
          <SettingItem
            label="Language"
            description="Select your preferred language"
          >
            <select
              value={settings.display.language}
              onChange={(e) => handleSettingChange('display', 'language', e.target.value)}
              className="px-3 py-2 border rounded-md focus:outline-none focus:ring-primary-500 focus:border-primary-500 transition-colors"
              style={{ 
                borderColor: 'var(--border-color)',
                backgroundColor: 'var(--bg-primary)',
                color: 'var(--text-primary)'
              }}
            >
              {availableLanguages.map(lang => (
                <option key={lang.code} value={lang.code}>{lang.name}</option>
              ))}
            </select>
          </SettingItem>
          
          <SettingItem
            label="Timezone"
            description="Set your local timezone"
          >
            <select
              value={settings.display.timezone}
              onChange={(e) => handleSettingChange('display', 'timezone', e.target.value)}
              className="px-3 py-2 border rounded-md focus:outline-none focus:ring-primary-500 focus:border-primary-500 transition-colors"
              style={{ 
                borderColor: 'var(--border-color)',
                backgroundColor: 'var(--bg-primary)',
                color: 'var(--text-primary)'
              }}
            >
              {availableTimezones.map(tz => (
                <option key={tz.id} value={tz.id}>{tz.name} ({tz.offset})</option>
              ))}
            </select>
          </SettingItem>
          
          <SettingItem
            label="Compact Mode"
            description="Use compact layout for better space utilization"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.display.compactMode}
                onChange={(e) => handleSettingChange('display', 'compactMode', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
          
          <SettingItem
            label="Show Avatars"
            description="Display user profile pictures"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.display.showAvatars}
                onChange={(e) => handleSettingChange('display', 'showAvatars', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
        </div>
      </SettingSection>

      {/* Security Settings */}
      <SettingSection title="Security Settings" icon={Shield}>
        <div className="space-y-4">
          <SettingItem
            label="Two-Factor Authentication"
            description="Add an extra layer of security to your account"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.security.twoFactor}
                onChange={(e) => handleSettingChange('security', 'twoFactor', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
          
          <SettingItem
            label="Session Timeout"
            description="Automatically log out after inactivity (minutes)"
          >
            <select
              value={settings.security.sessionTimeout}
              onChange={(e) => handleSettingChange('security', 'sessionTimeout', parseInt(e.target.value))}
              className="px-3 py-2 border rounded-md focus:outline-none focus:ring-primary-500 focus:border-primary-500 transition-colors"
              style={{ 
                borderColor: 'var(--border-color)',
                backgroundColor: 'var(--bg-primary)',
                color: 'var(--text-primary)'
              }}
            >
              <option value={15}>15 minutes</option>
              <option value={30}>30 minutes</option>
              <option value={60}>1 hour</option>
              <option value={120}>2 hours</option>
              <option value={240}>4 hours</option>
            </select>
          </SettingItem>
          
          <SettingItem
            label="Login Notifications"
            description="Get notified of new login attempts"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.security.loginNotifications}
                onChange={(e) => handleSettingChange('security', 'loginNotifications', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
        </div>
      </SettingSection>

      {/* Privacy Settings */}
      <SettingSection title="Privacy Settings" icon={Eye}>
        <div className="space-y-4">
          <SettingItem
            label="Profile Visibility"
            description="Control who can see your profile information"
          >
                         <select
               value={settings.privacy.profileVisibility}
               onChange={(e) => handleSettingChange('privacy', 'profileVisibility', e.target.value)}
               className="px-3 py-2 border rounded-md focus:outline-none focus:ring-primary-500 focus:border-primary-500 transition-colors"
               style={{ 
                 borderColor: 'var(--border-color)',
                 backgroundColor: 'var(--bg-primary)',
                 color: 'var(--text-primary)'
               }}
             >
              <option value="public">Public</option>
              <option value="team">Team Only</option>
              <option value="private">Private</option>
            </select>
          </SettingItem>
          
          <SettingItem
            label="Activity Visibility"
            description="Control who can see your activity"
          >
                         <select
               value={settings.privacy.activityVisibility}
               onChange={(e) => handleSettingChange('privacy', 'activityVisibility', e.target.value)}
               className="px-3 py-2 border rounded-md focus:outline-none focus:ring-primary-500 focus:border-primary-500 transition-colors"
               style={{ 
                 borderColor: 'var(--border-color)',
                 backgroundColor: 'var(--bg-primary)',
                 color: 'var(--text-primary)'
               }}
             >
              <option value="public">Public</option>
              <option value="team">Team Only</option>
              <option value="private">Private</option>
            </select>
          </SettingItem>
          
          <SettingItem
            label="Data Sharing"
            description="Allow sharing of your data for analytics"
          >
            <label className="relative inline-flex items-center cursor-pointer">
              <input
                type="checkbox"
                checked={settings.privacy.dataSharing}
                onChange={(e) => handleSettingChange('privacy', 'dataSharing', e.target.checked)}
                className="sr-only peer"
              />
              <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
            </label>
          </SettingItem>
        </div>
      </SettingSection>

      {/* Admin Settings */}
      {user?.role === 'ADMIN' && (
        <SettingSection title="System Settings" icon={Database}>
          <div className="space-y-4">
            <SettingItem
              label="System Maintenance"
              description="Enable maintenance mode for system updates"
            >
              <label className="relative inline-flex items-center cursor-pointer">
                <input
                  type="checkbox"
                  className="sr-only peer"
                />
                <div className="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-primary-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary-600"></div>
              </label>
            </SettingItem>
            
            <SettingItem
              label="Data Backup"
              description="Schedule automatic data backups"
            >
                             <select className="px-3 py-2 border rounded-md focus:outline-none focus:ring-primary-500 focus:border-primary-500 transition-colors" style={{ 
                 borderColor: 'var(--border-color)',
                 backgroundColor: 'var(--bg-primary)',
                 color: 'var(--text-primary)'
               }}>
                <option value="daily">Daily</option>
                <option value="weekly">Weekly</option>
                <option value="monthly">Monthly</option>
              </select>
            </SettingItem>
            
            <SettingItem
              label="API Rate Limiting"
              description="Set API request limits per user"
            >
                             <input
                 type="number"
                 placeholder="1000"
                 className="px-3 py-2 border rounded-md focus:outline-none focus:ring-primary-500 focus:border-primary-500 w-24 transition-colors"
                 style={{ 
                   borderColor: 'var(--border-color)',
                   backgroundColor: 'var(--bg-primary)',
                   color: 'var(--text-primary)'
                 }}
               />
            </SettingItem>
          </div>
        </SettingSection>
      )}

      {/* Settings Actions */}
      <div className="p-6 rounded-lg shadow border transition-colors" style={{ 
        backgroundColor: 'var(--bg-primary)',
        borderColor: 'var(--border-color)'
      }}>
        <div className="flex flex-wrap items-center justify-between gap-4">
          <div className="flex flex-wrap items-center gap-3">
            <button
              onClick={handleExportSettings}
              className="px-4 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 flex items-center"
            >
              <Download className="h-4 w-4 mr-2" />
              Export Settings
            </button>
            
            <label className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 flex items-center cursor-pointer">
              <Upload className="h-4 w-4 mr-2" />
              Import Settings
              <input
                type="file"
                accept=".json"
                onChange={handleImportSettings}
                className="hidden"
              />
            </label>
            
            <button
              onClick={handleResetToDefaults}
              className="px-4 py-2 bg-yellow-600 text-white rounded-md hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500 flex items-center"
            >
              <RotateCcw className="h-4 w-4 mr-2" />
              Reset to Defaults
            </button>
          </div>
          

        </div>
      </div>
    </div>
  );
};

export default Settings;
 