import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

class CustomerService {
  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add auth token interceptor
    this.api.interceptors.request.use((config) => {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    });
  }

  async getAllCustomers() {
    try {
      const response = await this.api.get('/customers');
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch customers');
    }
  }

  async getCustomerById(id) {
    try {
      const response = await this.api.get(`/customers/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch customer');
    }
  }

  async createCustomer(customerData) {
    try {
      const response = await this.api.post('/customers', customerData);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to create customer');
    }
  }

  async updateCustomer(id, customerData) {
    try {
      const response = await this.api.put(`/customers/${id}`, customerData);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to update customer');
    }
  }

  async deleteCustomer(id) {
    try {
      await this.api.delete(`/customers/${id}`);
      return true;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to delete customer');
    }
  }

  async searchCustomers(searchTerm) {
    try {
      const response = await this.api.get(`/customers/search/term?searchTerm=${searchTerm}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to search customers');
    }
  }

  async getCustomersByType(customerType) {
    try {
      const response = await this.api.get(`/customers/type/${customerType}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch customers by type');
    }
  }

  async getCustomersByStatus(status) {
    try {
      const response = await this.api.get(`/customers/status/${status}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch customers by status');
    }
  }

  async getCustomersByIndustry(industry) {
    try {
      const response = await this.api.get(`/customers/industry/${industry}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch customers by industry');
    }
  }

  async getCustomersByAssignedUser(userId) {
    try {
      const response = await this.api.get(`/customers/assigned/${userId}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch customers by assigned user');
    }
  }

  async getCustomerCountByType(customerType) {
    try {
      const response = await this.api.get(`/customers/analytics/type-count?customerType=${customerType}`);
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch customer count by type');
    }
  }

  async getCustomerCountByIndustry() {
    try {
      const response = await this.api.get('/customers/analytics/industry-count');
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data?.message || 'Failed to fetch customer count by industry');
    }
  }
}

export const customerService = new CustomerService();
