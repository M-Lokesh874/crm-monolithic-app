import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  // Helper function to fetch full user details
  const fetchUserDetails = async (token) => {
    try {
      authService.setAuthToken(token);
      const response = await authService.getCurrentUser();
      return response;
    } catch (error) {
      console.error('Error fetching user details:', error);
      return null;
    }
  };

  useEffect(() => {
    // Check if user is already logged in and validate token
    const checkAuthStatus = async () => {
      const token = localStorage.getItem('token');
      const userData = localStorage.getItem('user');
      
      if (token && userData) {
        try {
          // Fetch full user details from backend
          const fullUserData = await fetchUserDetails(token);
          
          if (fullUserData) {
            setUser(fullUserData);
            setIsAuthenticated(true);
          } else {
            // Token is invalid, clear storage
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            authService.removeAuthToken();
            setUser(null);
            setIsAuthenticated(false);
          }
        } catch (error) {
          console.error('Error validating token:', error);
          // Token validation failed, clear storage
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          authService.removeAuthToken();
          setUser(null);
          setIsAuthenticated(false);
        }
      }
      setLoading(false);
    };

    checkAuthStatus();
  }, []);

  const login = async (username, password) => {
    try {
      const response = await authService.login(username, password);
      const { token } = response;
      
      // Store token first
      localStorage.setItem('token', token);
      authService.setAuthToken(token);
      
      // Fetch full user details
      const fullUserData = await fetchUserDetails(token);
      
      if (fullUserData) {
        // Store full user data
        localStorage.setItem('user', JSON.stringify(fullUserData));
        setUser(fullUserData);
        setIsAuthenticated(true);
        return { success: true };
      } else {
        throw new Error('Failed to fetch user details');
      }
    } catch (error) {
      return { success: false, error: error.message };
    }
  };

  const register = async (userData) => {
    try {
      const response = await authService.register(userData);
      const { token } = response;
      
      // Store token first
      localStorage.setItem('token', token);
      authService.setAuthToken(token);
      
      // Fetch full user details
      const fullUserData = await fetchUserDetails(token);
      
      if (fullUserData) {
        // Store full user data
        localStorage.setItem('user', JSON.stringify(fullUserData));
        setUser(fullUserData);
        setIsAuthenticated(true);
        return { success: true };
      } else {
        throw new Error('Failed to fetch user details');
      }
    } catch (error) {
      return { success: false, error: error.message };
    }
  };

  const logout = async () => {
    try {
      await authService.logout();
      setUser(null);
      setIsAuthenticated(false);
    } catch (error) {
      console.error('Logout error:', error);
      // Even if logout fails, clear local state
      setUser(null);
      setIsAuthenticated(false);
    }
  };

  const updateUser = (userData) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };

  const refreshUser = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        const fullUserData = await fetchUserDetails(token);
        if (fullUserData) {
          setUser(fullUserData);
          localStorage.setItem('user', JSON.stringify(fullUserData));
          return true;
        } else {
          logout();
          return false;
        }
      } else {
        logout();
        return false;
      }
    } catch (error) {
      console.error('Error refreshing user:', error);
      logout();
      return false;
    }
  };

  const value = {
    user,
    isAuthenticated,
    loading,
    login,
    register,
    logout,
    updateUser,
    refreshUser,
    // Add a method to check if we're still loading auth state
    isAuthLoading: loading,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
