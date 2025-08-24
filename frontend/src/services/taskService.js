import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

/**
 * Task Service - Handles all task-related API calls
 */
class TaskService {
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
     * Get all tasks
     * @returns {Promise<Array>} List of tasks
     */
    async getAllTasks() {
        try {
            const response = await this.api.get('/tasks');
            return response.data;
        } catch (error) {
            console.error('Error fetching tasks:', error);
            throw error;
        }
    }

    /**
     * Get task by ID
     * @param {number} id - Task ID
     * @returns {Promise<Object>} Task data
     */
    async getTaskById(id) {
        try {
            const response = await this.api.get(`/tasks/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching task ${id}:`, error);
            throw error;
        }
    }

    /**
     * Create a new task
     * @param {Object} taskData - Task creation data
     * @returns {Promise<Object>} Created task
     */
    async createTask(taskData) {
        try {
            const response = await this.api.post('/tasks', taskData);
            return response.data;
        } catch (error) {
            console.error('Error creating task:', error);
            throw error;
        }
    }

    /**
     * Update an existing task
     * @param {number} id - Task ID
     * @param {Object} taskData - Task update data
     * @returns {Promise<Object>} Updated task
     */
    async updateTask(id, taskData) {
        try {
            const response = await this.api.put(`/tasks/${id}`, taskData);
            return response.data;
        } catch (error) {
            console.error(`Error updating task ${id}:`, error);
            throw error;
        }
    }

    /**
     * Delete a task
     * @param {number} id - Task ID
     * @returns {Promise<void>}
     */
    async deleteTask(id) {
        try {
            await this.api.delete(`/tasks/${id}`);
        } catch (error) {
            console.error(`Error deleting task ${id}:`, error);
            throw error;
        }
    }

    /**
     * Get tasks assigned to a specific user
     * @param {number} userId - User ID
     * @returns {Promise<Array>} List of tasks
     */
    async getTasksByAssignedUser(userId) {
        try {
            const response = await this.api.get(`/tasks/assigned-to/${userId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching tasks for user ${userId}:`, error);
            throw error;
        }
    }

    /**
     * Get tasks created by a specific user
     * @param {number} userId - User ID
     * @returns {Promise<Array>} List of tasks
     */
    async getTasksByCreatedUser(userId) {
        try {
            const response = await this.api.get(`/tasks/created-by/${userId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching tasks created by user ${userId}:`, error);
            throw error;
        }
    }

    /**
     * Get tasks by status
     * @param {string} status - Task status
     * @returns {Promise<Array>} List of tasks
     */
    async getTasksByStatus(status) {
        try {
            const response = await this.api.get(`/tasks/status/${status}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching tasks with status ${status}:`, error);
            throw error;
        }
    }

    /**
     * Get tasks by priority
     * @param {string} priority - Task priority
     * @returns {Promise<Array>} List of tasks
     */
    async getTasksByPriority(priority) {
        try {
            const response = await this.api.get(`/tasks/priority/${priority}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching tasks with priority ${priority}:`, error);
            throw error;
        }
    }

    /**
     * Get overdue tasks
     * @returns {Promise<Array>} List of overdue tasks
     */
    async getOverdueTasks() {
        try {
            const response = await this.api.get('/tasks/overdue');
            return response.data;
        } catch (error) {
            console.error('Error fetching overdue tasks:', error);
            throw error;
        }
    }

    /**
     * Get overdue tasks for a specific user
     * @param {number} userId - User ID
     * @returns {Promise<Array>} List of overdue tasks
     */
    async getOverdueTasksByUser(userId) {
        try {
            const response = await this.api.get(`/tasks/overdue/user/${userId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching overdue tasks for user ${userId}:`, error);
            throw error;
        }
    }

    /**
     * Get tasks due within a date range
     * @param {Date} startDate - Start date
     * @param {Date} endDate - End date
     * @returns {Promise<Array>} List of tasks
     */
    async getTasksByDueDateRange(startDate, endDate) {
        try {
            const response = await this.api.get('/tasks/due-date-range', {
                params: {
                    startDate: startDate.toISOString(),
                    endDate: endDate.toISOString()
                }
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching tasks by due date range:', error);
            throw error;
        }
    }

    /**
     * Get tasks for a specific customer
     * @param {number} customerId - Customer ID
     * @returns {Promise<Array>} List of tasks
     */
    async getTasksByCustomer(customerId) {
        try {
            const response = await this.api.get(`/tasks/customer/${customerId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching tasks for customer ${customerId}:`, error);
            throw error;
        }
    }

    /**
     * Get tasks for a specific lead
     * @param {number} leadId - Lead ID
     * @returns {Promise<Array>} List of tasks
     */
    async getTasksByLead(leadId) {
        try {
            const response = await this.api.get(`/tasks/lead/${leadId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching tasks for lead ${leadId}:`, error);
            throw error;
        }
    }

    /**
     * Search tasks by title or description
     * @param {string} searchTerm - Search term
     * @returns {Promise<Array>} List of matching tasks
     */
    async searchTasks(searchTerm) {
        try {
            const response = await this.api.get('/tasks/search', {
                params: { searchTerm }
            });
            return response.data;
        } catch (error) {
            console.error(`Error searching tasks with term "${searchTerm}":`, error);
            throw error;
        }
    }

    /**
     * Update task status
     * @param {number} id - Task ID
     * @param {string} status - New status
     * @returns {Promise<Object>} Updated task
     */
    async updateTaskStatus(id, status) {
        try {
            const response = await this.api.patch(`/tasks/${id}/status`, null, {
                params: { status }
            });
            return response.data;
        } catch (error) {
            console.error(`Error updating task ${id} status to ${status}:`, error);
            throw error;
        }
    }

    /**
     * Assign task to a user
     * @param {number} id - Task ID
     * @param {number} userId - User ID to assign to
     * @returns {Promise<Object>} Updated task
     */
    async assignTask(id, userId) {
        try {
            const response = await this.api.patch(`/tasks/${id}/assign`, null, {
                params: { userId }
            });
            return response.data;
        } catch (error) {
            console.error(`Error assigning task ${id} to user ${userId}:`, error);
            throw error;
        }
    }

    /**
     * Get task statistics for a user
     * @param {number} userId - User ID
     * @returns {Promise<Object>} Task statistics
     */
    async getUserTaskStatistics(userId) {
        try {
            const response = await this.api.get(`/tasks/statistics/user/${userId}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching task statistics for user ${userId}:`, error);
            throw error;
        }
    }

    /**
     * Get task statistics for all users (admin only)
     * @returns {Promise<Array>} List of task statistics for all users
     */
    async getAllTaskStatistics() {
        try {
            const response = await this.api.get('/tasks/statistics/all');
            return response.data;
        } catch (error) {
            console.error('Error fetching all task statistics:', error);
            throw error;
        }
    }

    /**
     * Get tasks for current user (assigned and created)
     * @param {number} userId - Current user ID
     * @returns {Promise<Object>} Object with assigned and created tasks
     */
    async getCurrentUserTasks(userId) {
        try {
            const [assignedTasks, createdTasks] = await Promise.all([
                this.getTasksByAssignedUser(userId),
                this.getTasksByCreatedUser(userId)
            ]);

            return {
                assigned: assignedTasks,
                created: createdTasks
            };
        } catch (error) {
            console.error(`Error fetching current user tasks for user ${userId}:`, error);
            throw error;
        }
    }

    /**
     * Get dashboard task summary for current user
     * @param {number} userId - Current user ID
     * @returns {Promise<Object>} Task summary data
     */
    async getDashboardTaskSummary(userId) {
        try {
            const [assignedTasks, overdueTasks, statistics] = await Promise.all([
                this.getTasksByAssignedUser(userId),
                this.getOverdueTasksByUser(userId),
                this.getUserTaskStatistics(userId)
            ]);

            return {
                totalAssigned: assignedTasks.length,
                overdue: overdueTasks.length,
                statistics: statistics
            };
        } catch (error) {
            console.error(`Error fetching dashboard task summary for user ${userId}:`, error);
            throw error;
        }
    }
}

export default new TaskService();
