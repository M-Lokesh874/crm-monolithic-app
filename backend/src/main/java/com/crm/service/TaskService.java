package com.crm.service;

import com.crm.dto.TaskCreateDTO;
import com.crm.dto.TaskDTO;
import com.crm.dto.TaskUpdateDTO;
import com.crm.entity.Task;
import com.crm.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Task management operations.
 * Provides business logic for task creation, updates, and queries.
 */
public interface TaskService {

    /**
     * Create a new task.
     *
     * @param taskCreateDTO the task creation data
     * @param createdBy the user creating the task
     * @return the created task DTO
     */
    TaskDTO createTask(TaskCreateDTO taskCreateDTO, User createdBy);

    /**
     * Get all tasks.
     *
     * @return list of all task DTOs
     */
    List<TaskDTO> getAllTasks();

    /**
     * Get a task by its ID.
     *
     * @param id the task ID
     * @return the task DTO if found
     */
    TaskDTO getTaskById(Long id);

    /**
     * Update an existing task.
     *
     * @param id the task ID
     * @param taskUpdateDTO the task update data
     * @return the updated task DTO
     */
    TaskDTO updateTask(Long id, TaskUpdateDTO taskUpdateDTO);

    /**
     * Delete a task.
     *
     * @param id the task ID
     */
    void deleteTask(Long id);

    /**
     * Get all tasks assigned to a specific user.
     *
     * @param userId the user ID
     * @return list of task DTOs assigned to the user
     */
    List<TaskDTO> getTasksByAssignedUser(Long userId);

    /**
     * Get all tasks created by a specific user.
     *
     * @param userId the user ID
     * @return list of task DTOs created by the user
     */
    List<TaskDTO> getTasksByCreatedUser(Long userId);

    /**
     * Get all tasks with a specific status.
     *
     * @param status the task status
     * @return list of task DTOs with the specified status
     */
    List<TaskDTO> getTasksByStatus(Task.TaskStatus status);

    /**
     * Get all tasks with a specific priority.
     *
     * @param priority the task priority
     * @return list of task DTOs with the specified priority
     */
    List<TaskDTO> getTasksByPriority(Task.TaskPriority priority);

    /**
     * Get all tasks with a specific type.
     *
     * @param taskType the task type
     * @return list of task DTOs with the specified type
     */
    List<TaskDTO> getTasksByType(Task.TaskType taskType);

    /**
     * Get all overdue tasks.
     *
     * @return list of overdue task DTOs
     */
    List<TaskDTO> getOverdueTasks();

    /**
     * Get all overdue tasks for a specific user.
     *
     * @param userId the user ID
     * @return list of overdue task DTOs for the user
     */
    List<TaskDTO> getOverdueTasksByUser(Long userId);

    /**
     * Get tasks due within a date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of task DTOs due within the range
     */
    List<TaskDTO> getTasksByDueDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get tasks for a specific customer.
     *
     * @param customerId the customer ID
     * @return list of task DTOs for the customer
     */
    List<TaskDTO> getTasksByCustomer(Long customerId);

    /**
     * Get tasks for a specific lead.
     *
     * @param leadId the lead ID
     * @return list of task DTOs for the lead
     */
    List<TaskDTO> getTasksByLead(Long leadId);

    /**
     * Search tasks by title or description.
     *
     * @param searchTerm the search term
     * @return list of matching task DTOs
     */
    List<TaskDTO> searchTasks(String searchTerm);

    /**
     * Update task status.
     *
     * @param id the task ID
     * @param status the new status
     * @return the updated task DTO
     */
    TaskDTO updateTaskStatus(Long id, Task.TaskStatus status);

    /**
     * Assign task to a user.
     *
     * @param id the task ID
     * @param userId the user ID to assign to
     * @return the updated task DTO
     */
    TaskDTO assignTask(Long id, Long userId);

    /**
     * Get task statistics for a user.
     *
     * @param userId the user ID
     * @return task statistics object
     */
    TaskStatistics getTaskStatistics(Long userId);

    /**
     * Get task statistics for all users (admin only).
     *
     * @return task statistics for all users
     */
    List<TaskStatistics> getAllTaskStatistics();

    /**
     * Task statistics data class.
     */
    class TaskStatistics {
        private Long userId;
        private String userName;
        private long totalTasks;
        private long pendingTasks;
        private long inProgressTasks;
        private long completedTasks;
        private long overdueTasks;

        // Constructors, getters, and setters
        public TaskStatistics() {}

        public TaskStatistics(Long userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public long getTotalTasks() { return totalTasks; }
        public void setTotalTasks(long totalTasks) { this.totalTasks = totalTasks; }

        public long getPendingTasks() { return pendingTasks; }
        public void setPendingTasks(long pendingTasks) { this.pendingTasks = pendingTasks; }

        public long getInProgressTasks() { return inProgressTasks; }
        public void setInProgressTasks(long inProgressTasks) { this.inProgressTasks = inProgressTasks; }

        public long getCompletedTasks() { return completedTasks; }
        public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }

        public long getOverdueTasks() { return overdueTasks; }
        public void setOverdueTasks(long overdueTasks) { this.overdueTasks = overdueTasks; }
    }
}
