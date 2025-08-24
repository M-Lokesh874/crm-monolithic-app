package com.crm.dto;

import com.crm.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Data Transfer Object for Task entity.
 * Used for transferring task data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task data transfer object")
public class TaskDTO {

    @Schema(description = "Unique identifier for the task", example = "1")
    private Long id;

    @NotBlank(message = "Task title is required")
    @Size(min = 3, max = 255, message = "Task title must be between 3 and 255 characters")
    @Schema(description = "Title of the task", example = "Follow up with TechCorp proposal")
    private String title;

    @Size(max = 1000, message = "Task description cannot exceed 1000 characters")
    @Schema(description = "Detailed description of the task", example = "Send updated proposal and schedule demo call")
    private String description;

    @NotNull(message = "Task type is required")
    @Schema(description = "Type of task", example = "FOLLOW_UP")
    private Task.TaskType taskType;

    @NotNull(message = "Task priority is required")
    @Schema(description = "Priority level of the task", example = "HIGH")
    private Task.TaskPriority priority;

    @NotNull(message = "Due date is required")
    @Schema(description = "Due date for the task", example = "2024-01-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull(message = "Task status is required")
    @Schema(description = "Current status of the task", example = "IN_PROGRESS")
    private Task.TaskStatus status;

    @Schema(description = "ID of user assigned to the task", example = "1")
    private Long assignedToId;

    @Schema(description = "Name of user assigned to the task", example = "John Doe")
    private String assignedToName;

    @Schema(description = "ID of customer associated with the task", example = "1")
    private Long customerId;

    @Schema(description = "Name of customer associated with the task", example = "TechCorp")
    private String customerName;

    @Schema(description = "ID of lead associated with the task", example = "1")
    private Long leadId;

    @Schema(description = "Title of lead associated with the task", example = "TechCorp Proposal")
    private String leadTitle;

    @Schema(description = "ID of user who created the task", example = "1")
    private Long createdById;

    @Schema(description = "Name of user who created the task", example = "Jane Smith")
    private String createdByName;

    @Schema(description = "Timestamp when the task was created", example = "2024-01-10T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the task was last updated", example = "2024-01-12T14:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Timestamp when the task was completed", example = "2024-01-15T16:00:00")
    private LocalDateTime completedAt;

    @Schema(description = "Estimated hours to complete the task", example = "2.5")
    private Double estimatedHours;

    @Schema(description = "Actual hours spent on the task", example = "3.0")
    private Double actualHours;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Schema(description = "Additional notes about the task")
    private String notes;

    @Schema(description = "Flag indicating if the task is overdue", example = "false")
    private boolean overdue;
}
