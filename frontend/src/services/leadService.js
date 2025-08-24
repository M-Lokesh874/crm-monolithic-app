import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

class LeadService {
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

  // Get all leads
  async getAllLeads() {
    try {
      const response = await this.api.get('/leads');
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch leads');
    }
  }

  // Get lead by ID
  async getLeadById(id) {
    try {
      const response = await this.api.get(`/leads/${id}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch lead');
    }
  }

  // Create new lead
  async createLead(leadData) {
    try {
      const response = await this.api.post('/leads', leadData);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to create lead');
    }
  }

  // Update lead (full update - requires all fields)
  async updateLead(id, leadData) {
    try {
      const response = await this.api.put(`/leads/${id}`, leadData);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to update lead');
    }
  }

  // Partial update lead (only update provided fields)
  async partialUpdateLead(id, leadData) {
    try {
      const response = await this.api.patch(`/leads/${id}`, leadData);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to partially update lead');
    }
  }

  // Delete lead
  async deleteLead(id) {
    try {
      const response = await this.api.delete(`/leads/${id}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to delete lead');
    }
  }

  // Get leads by status
  async getLeadsByStatus(status) {
    try {
      const response = await this.api.get(`/leads/status/${status}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch leads by status');
    }
  }

  // Get leads by source
  async getLeadsBySource(source) {
    try {
      const response = await this.api.get(`/leads/source/${source}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch leads by source');
    }
  }

  // Search leads
  async searchLeads(query) {
    try {
      const response = await this.api.get(`/leads/search?q=${encodeURIComponent(query)}`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to search leads');
    }
  }

  // Get lead statistics
  async getLeadStatistics() {
    try {
      const response = await this.api.get('/leads/statistics');
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch lead statistics');
    }
  }

  // Update lead status
  async updateLeadStatus(id, status) {
    try {
      const response = await this.api.patch(`/leads/${id}`, { leadStatus: status });
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to update lead status');
    }
  }

  // Add note to lead
  async addNoteToLead(id, note) {
    try {
      const response = await this.api.post(`/leads/${id}/notes`, { note });
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to add note to lead');
    }
  }

  // Get lead notes
  async getLeadNotes(id) {
    try {
      const response = await this.api.get(`/leads/${id}/notes`);
      return response.data;
    } catch (error) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to fetch lead notes');
    }
  }
}

export const leadService = new LeadService();
