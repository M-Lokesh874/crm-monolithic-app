import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

class AuthService {
  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add response interceptor to handle 401 responses
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          // Token is invalid, clear local storage
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          this.removeAuthToken();
          
          // Redirect to login if we're not already there
          if (window.location.pathname !== '/login') {
            window.location.href = '/login';
          }
        }
        return Promise.reject(error);
      }
    );
  }

  setAuthToken(token) {
    if (token) {
      this.api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    } else {
      delete this.api.defaults.headers.common['Authorization'];
    }
  }

  removeAuthToken() {
    delete this.api.defaults.headers.common['Authorization'];
  }

  async login(username, password) {
    try {
      const response = await this.api.post('/auth/login', {
        username,
        password,
      });
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Login failed. Please check your credentials.');
    }
  }

  async register(userData) {
    try {
      const response = await this.api.post('/auth/register', {
        username: userData.username,
        email: userData.email,
        password: userData.password,
        firstName: userData.firstName,
        lastName: userData.lastName,
      });
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Registration failed. Please try again.');
    }
  }

  async validateToken() {
    try {
      // Use the current user endpoint to validate token
      // This will return user data if token is valid, or 401 if invalid
      const response = await this.api.get('/auth/me');
      return response.status === 200;
    } catch (error) {
      // If we get a 401 or any other error, token is invalid
      return false;
    }
  }

  /**
   * Get current authenticated user details
   * @returns {Promise<Object>} Current user data
   */
  async getCurrentUser() {
    try {
      const response = await this.api.get('/auth/me');
      return response.data;
    } catch (error) {
      console.error('Error fetching current user:', error);
      throw error;
    }
  }

  async logout() {
    try {
      // Call backend logout endpoint for audit logging
      await this.api.post('/auth/logout');
    } catch (error) {
      // Even if backend call fails, we still logout locally
      console.log('Backend logout failed, but proceeding with local logout');
    } finally {
      // Always clear local storage and remove token
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      this.removeAuthToken();
    }
    return true;
  }
}

export const authService = new AuthService();
