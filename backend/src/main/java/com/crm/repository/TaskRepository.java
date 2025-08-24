package com.crm.repository;

import com.crm.entity.Task;
import com.crm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Task entity.
 * Provides data access methods for task management operations.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find all tasks assigned to a specific user.
     *
     * @param assignedTo the user assigned to the tasks
     * @return list of tasks assigned to the user
     */
    List<Task> findByAssignedTo(User assignedTo);

    /**
     * Find all tasks created by a specific user.
     *
     * @param createdBy the user who created the tasks
     * @return list of tasks created by the user
     */
    List<Task> findByCreatedBy(User createdBy);

    /**
     * Find all tasks with a specific status.
     *
     * @param status the task status to search for
     * @return list of tasks with the specified status
     */
    List<Task> findByStatus(Task.TaskStatus status);

    /**
     * Find all tasks with a specific priority.
     *
     * @param priority the task priority to search for
     * @return list of tasks with the specified priority
     */
    List<Task> findByPriority(Task.TaskPriority priority);

    /**
     * Find all tasks with a specific type.
     *
     * @param taskType the task type to search for
     * @return list of tasks with the specified type
     */
    List<Task> findByTaskType(Task.TaskType taskType);

    /**
     * Find all tasks due before a specific date.
     *
     * @param dueDate the due date to search before
     * @return list of tasks due before the specified date
     */
    List<Task> findByDueDateBefore(LocalDateTime dueDate);

    /**
     * Find all tasks due between two dates.
     *
     * @param startDate the start date for the range
     * @param endDate the end date for the range
     * @return list of tasks due within the specified date range
     */
    List<Task> findByDueDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find all overdue tasks (due date is in the past and status is not completed).
     *
     * @param currentDate the current date to compare against
     * @return list of overdue tasks
     */
    @Query("SELECT t FROM Task t WHERE t.dueDate < :currentDate AND t.status != 'COMPLETED' AND t.status != 'CANCELLED'")
    List<Task> findOverdueTasks(@Param("currentDate") LocalDateTime currentDate);

    /**
     * Find all tasks assigned to a user with a specific status.
     *
     * @param assignedTo the user assigned to the tasks
     * @param status the task status to search for
     * @return list of tasks matching both criteria
     */
    List<Task> findByAssignedToAndStatus(User assignedTo, Task.TaskStatus status);

    /**
     * Find all tasks for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return list of tasks associated with the customer
     */
    List<Task> findByCustomerId(Long customerId);

    /**
     * Find all tasks for a specific lead.
     *
     * @param leadId the ID of the lead
     * @return list of tasks associated with the lead
     */
    List<Task> findByLeadId(Long leadId);

    /**
     * Find tasks by title containing the specified text (case-insensitive).
     *
     * @param title the text to search for in task titles
     * @return list of tasks with matching titles
     */
    List<Task> findByTitleContainingIgnoreCase(String title);

    /**
     * Find tasks by description containing the specified text (case-insensitive).
     *
     * @param description the text to search for in task descriptions
     * @return list of tasks with matching descriptions
     */
    List<Task> findByDescriptionContainingIgnoreCase(String description);

    /**
     * Find tasks by title or description containing the specified text (case-insensitive).
     *
     * @param searchTerm the text to search for
     * @return list of tasks with matching title or description
     */
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Task> findByTitleOrDescriptionContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Count tasks by status for a specific user.
     *
     * @param assignedTo the user assigned to the tasks
     * @param status the task status to count
     * @return count of tasks with the specified status for the user
     */
    long countByAssignedToAndStatus(User assignedTo, Task.TaskStatus status);

    /**
     * Count all tasks assigned to a specific user.
     *
     * @param assignedTo the user assigned to the tasks
     * @return count of tasks assigned to the user
     */
    long countByAssignedTo(User assignedTo);

    /**
     * Count overdue tasks for a specific user.
     *
     * @param assignedTo the user assigned to the tasks
     * @param currentDate the current date to compare against
     * @return count of overdue tasks for the user
     */
    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo = :assignedTo AND t.dueDate < :currentDate AND t.status != 'COMPLETED' AND t.status != 'CANCELLED'")
    long countOverdueTasksByUser(@Param("assignedTo") User assignedTo, @Param("currentDate") LocalDateTime currentDate);

    /**
     * Find overdue tasks for a specific user.
     *
     * @param assignedTo the user assigned to the tasks
     * @param currentDate the current date to compare against
     * @return list of overdue tasks for the user
     */
    @Query("SELECT t FROM Task t WHERE t.assignedTo = :assignedTo AND t.dueDate < :currentDate AND t.status != 'COMPLETED' AND t.status != 'CANCELLED'")
    List<Task> findOverdueTasksByUser(@Param("assignedTo") User assignedTo, @Param("currentDate") LocalDateTime currentDate);

    /**
     * Find tasks with optional filters.
     *
     * @param assignedTo optional user filter
     * @param status optional status filter
     * @param priority optional priority filter
     * @param taskType optional task type filter
     * @return list of tasks matching the filters
     */
    @Query("SELECT t FROM Task t WHERE " +
           "(:assignedTo IS NULL OR t.assignedTo = :assignedTo) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:taskType IS NULL OR t.taskType = :taskType)")
    List<Task> findTasksWithFilters(
            @Param("assignedTo") User assignedTo,
            @Param("status") Task.TaskStatus status,
            @Param("priority") Task.TaskPriority priority,
            @Param("taskType") Task.TaskType taskType
    );
}
