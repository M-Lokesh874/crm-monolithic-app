import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

class UserService {
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

  // Get all users
  async getAllUsers() {
    try {
      const response = await this.api.get('/users');
      return response.data;
    } catch (error) {
      if (error.response?.status === 403) {
        throw new Error('Access denied. You do not have permission to view users.');
      } else if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch users');
    }
  }

  async getUsersByRole(role) {
    try {
      const response = await this.api.get(`/users/role/${role}`);
      return response.data;
    } catch (error) {
      if (error.response?.status === 403) {
        throw new Error('Access denied. You do not have permission to view users.');
      } else if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch users by role');
    }
  }

  // Get user by ID
  async getUserById(id) {
    try {
      const response = await this.api.get(`/users/${id}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch user');
    }
  }

  // Get user by username
  async getUserByUsername(username) {
    try {
      const response = await this.api.get(`/users/username/${username}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch user');
    }
  }

  // Create new user
  async createUser(userData) {
    try {
      const response = await this.api.post('/users', userData);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to create user');
    }
  }

  // Update user
  async updateUser(id, userData) {
    try {
      const response = await this.api.put(`/users/${id}`, userData);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to update user');
    }
  }

  // Partial update user (PATCH)
  async partialUpdateUser(id, userData) {
    try {
      const response = await this.api.patch(`/users/${id}`, userData);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to update user');
    }
  }

  // Delete user (soft delete)
  async deleteUser(id) {
    try {
      const response = await this.api.delete(`/users/${id}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to delete user');
    }
  }



  // Get active users
  async getActiveUsers() {
    try {
      const response = await this.api.get('/users/active');
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch active users');
    }
  }

  // Search users
  async searchUsers(searchTerm) {
    try {
      const response = await this.api.get(`/users/search?searchTerm=${encodeURIComponent(searchTerm)}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to search users');
    }
  }

  // Check if username exists
  async checkUsernameExists(username) {
    try {
      const response = await this.api.get(`/users/check/username/${username}`);
      return response.data;
    } catch (error) {
      return false;
    }
  }

  // Check if email exists
  async checkEmailExists(email) {
    try {
      const response = await this.api.get(`/users/check/email/${email}`);
      return response.data;
    } catch (error) {
      return false;
    }
  }
}

export const userService = new UserService();
