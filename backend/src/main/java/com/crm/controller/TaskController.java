package com.crm.controller;

import com.crm.dto.TaskCreateDTO;
import com.crm.dto.TaskDTO;
import com.crm.dto.TaskUpdateDTO;
import com.crm.entity.Task;
import com.crm.entity.User;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.UserRepository;
import com.crm.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for Task management operations.
 * Provides endpoints for creating, reading, updating, and deleting tasks.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task Management", description = "APIs for managing tasks and activities")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    /**
     * Create a new task.
     *
     * @param taskCreateDTO the task creation data
     * @param userDetails the authenticated user details
     * @return the created task
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Create a new task", description = "Creates a new task with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TaskDTO> createTask(
            @Valid @RequestBody TaskCreateDTO taskCreateDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Creating new task: {}", taskCreateDTO.getTitle());
        
        // Get the authenticated user from UserDetails
        String username = userDetails.getUsername();
        User createdBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        TaskDTO createdTask = taskService.createTask(taskCreateDTO, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Get all tasks.
     *
     * @return list of all tasks
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.info("Fetching all tasks");
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get a task by its ID.
     *
     * @param id the task ID
     * @return the task if found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get task by ID", description = "Retrieves a specific task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TaskDTO> getTaskById(
            @Parameter(description = "ID of the task to retrieve") @PathVariable Long id) {
        
        log.info("Fetching task with id: {}", id);
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * Update an existing task.
     *
     * @param id the task ID
     * @param taskUpdateDTO the task update data
     * @return the updated task
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Update a task", description = "Updates an existing task with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TaskDTO> updateTask(
            @Parameter(description = "ID of the task to update") @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDTO taskUpdateDTO) {
        
        log.info("Updating task with id: {}", id);
        TaskDTO updatedTask = taskService.updateTask(id, taskUpdateDTO);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Delete a task.
     *
     * @param id the task ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Delete a task", description = "Deletes a task from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete") @PathVariable Long id) {
        
        log.info("Deleting task with id: {}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get tasks assigned to a specific user.
     *
     * @param userId the user ID
     * @return list of tasks assigned to the user
     */
    @GetMapping("/assigned-to/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get tasks by assigned user", description = "Retrieves all tasks assigned to a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> getTasksByAssignedUser(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        
        log.info("Fetching tasks assigned to user with id: {}", userId);
        List<TaskDTO> tasks = taskService.getTasksByAssignedUser(userId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get tasks created by a specific user.
     *
     * @param userId the user ID
     * @return list of tasks created by the user
     */
    @GetMapping("/created-by/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Get tasks by creator", description = "Retrieves all tasks created by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> getTasksByCreatedUser(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        
        log.info("Fetching tasks created by user with id: {}", userId);
        List<TaskDTO> tasks = taskService.getTasksByCreatedUser(userId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get tasks by status.
     *
     * @param status the task status
     * @return list of tasks with the specified status
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get tasks by status", description = "Retrieves all tasks with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(
            @Parameter(description = "Task status to filter by") @PathVariable Task.TaskStatus status) {
        
        log.info("Fetching tasks with status: {}", status);
        List<TaskDTO> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get tasks by priority.
     *
     * @param priority the task priority
     * @return list of tasks with the specified priority
     */
    @GetMapping("/priority/{priority}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get tasks by priority", description = "Retrieves all tasks with a specific priority")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid priority"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> getTasksByPriority(
            @Parameter(description = "Task priority to filter by") @PathVariable Task.TaskPriority priority) {
        
        log.info("Fetching tasks with priority: {}", priority);
        List<TaskDTO> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get overdue tasks.
     *
     * @return list of overdue tasks
     */
    @GetMapping("/overdue")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get overdue tasks", description = "Retrieves all overdue tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> getOverdueTasks() {
        log.info("Fetching overdue tasks");
        List<TaskDTO> tasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get overdue tasks for a specific user.
     *
     * @param userId the user ID
     * @return list of overdue tasks for the user
     */
    @GetMapping("/overdue/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get overdue tasks by user", description = "Retrieves all overdue tasks for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overdue tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> getOverdueTasksByUser(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        
        log.info("Fetching overdue tasks for user with id: {}", userId);
        List<TaskDTO> tasks = taskService.getOverdueTasksByUser(userId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Get tasks due within a date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of tasks due within the range
     */
    @GetMapping("/due-date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get tasks by due date range", description = "Retrieves all tasks due within a specified date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> getTasksByDueDateRange(
            @Parameter(description = "Start date for the range") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for the range") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("Fetching tasks due between {} and {}", startDate, endDate);
        List<TaskDTO> tasks = taskService.getTasksByDueDateRange(startDate, endDate);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Search tasks by title or description.
     *
     * @param searchTerm the search term
     * @return list of matching tasks
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Search tasks", description = "Searches tasks by title or description containing the search term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search term"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskDTO>> searchTasks(
            @Parameter(description = "Search term to look for in task titles and descriptions") 
            @RequestParam String searchTerm) {
        
        log.info("Searching tasks with term: {}", searchTerm);
        List<TaskDTO> tasks = taskService.searchTasks(searchTerm);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Update task status.
     *
     * @param id the task ID
     * @param status the new status
     * @return the updated task
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Update task status", description = "Updates the status of a specific task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task status updated successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @Parameter(description = "ID of the task") @PathVariable Long id,
            @Parameter(description = "New status for the task") @RequestParam Task.TaskStatus status) {
        
        log.info("Updating task status to {} for task with id: {}", status, id);
        TaskDTO updatedTask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Assign task to a user.
     *
     * @param id the task ID
     * @param userId the user ID to assign to
     * @return the updated task
     */
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Assign task to user", description = "Assigns a task to a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task assigned successfully",
                    content = @Content(schema = @Schema(implementation = TaskDTO.class))),
            @ApiResponse(responseCode = "404", description = "Task or user not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TaskDTO> assignTask(
            @Parameter(description = "ID of the task") @PathVariable Long id,
            @Parameter(description = "ID of the user to assign the task to") @RequestParam Long userId) {
        
        log.info("Assigning task with id: {} to user with id: {}", id, userId);
        TaskDTO updatedTask = taskService.assignTask(id, userId);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Get task statistics for a user.
     *
     * @param userId the user ID
     * @return task statistics for the user
     */
    @GetMapping("/statistics/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get user task statistics", description = "Retrieves task statistics for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskService.TaskStatistics.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<TaskService.TaskStatistics> getUserTaskStatistics(
            @Parameter(description = "ID of the user") @PathVariable Long userId) {
        
        log.info("Fetching task statistics for user with id: {}", userId);
        TaskService.TaskStatistics stats = taskService.getTaskStatistics(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get task statistics for all users (admin only).
     *
     * @return task statistics for all users
     */
    @GetMapping("/statistics/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users task statistics", description = "Retrieves task statistics for all users (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TaskService.TaskStatistics.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<List<TaskService.TaskStatistics>> getAllTaskStatistics() {
        log.info("Fetching task statistics for all users");
        List<TaskService.TaskStatistics> stats = taskService.getAllTaskStatistics();
        return ResponseEntity.ok(stats);
    }
}
