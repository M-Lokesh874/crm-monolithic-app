package com.crm.dto;

import com.crm.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Data Transfer Object for creating new tasks.
 * Contains only the fields required for task creation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data for creating a new task")
public class TaskCreateDTO {

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

    @Schema(description = "ID of user assigned to the task", example = "1")
    private Long assignedToId;

    @Schema(description = "ID of customer associated with the task", example = "1")
    private Long customerId;

    @Schema(description = "ID of lead associated with the task", example = "1")
    private Long leadId;

    @Schema(description = "Estimated hours to complete the task", example = "2.5")
    private Double estimatedHours;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Schema(description = "Additional notes about the task")
    private String notes;
}
