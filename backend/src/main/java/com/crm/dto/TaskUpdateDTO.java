package com.crm.dto;

import com.crm.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Data Transfer Object for updating existing tasks.
 * All fields are optional to allow partial updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data for updating an existing task")
public class TaskUpdateDTO {

    @Size(min = 3, max = 255, message = "Task title must be between 3 and 255 characters")
    @Schema(description = "Title of the task", example = "Follow up with TechCorp proposal")
    private String title;

    @Size(max = 1000, message = "Task description cannot exceed 1000 characters")
    @Schema(description = "Detailed description of the task", example = "Send updated proposal and schedule demo call")
    private String description;

    @Schema(description = "Type of task", example = "FOLLOW_UP")
    private Task.TaskType taskType;

    @Schema(description = "Priority level of the task", example = "HIGH")
    private Task.TaskPriority priority;

    @Schema(description = "Due date for the task", example = "2024-01-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @Schema(description = "Current status of the task", example = "IN_PROGRESS")
    private Task.TaskStatus status;

    @Schema(description = "ID of user assigned to the task", example = "1")
    private Long assignedToId;

    @Schema(description = "ID of customer associated with the task", example = "1")
    private Long customerId;

    @Schema(description = "ID of lead associated with the task", example = "1")
    private Long leadId;

    @Schema(description = "Estimated hours to complete the task", example = "2.5")
    private Double estimatedHours;

    @Schema(description = "Actual hours spent on the task", example = "3.0")
    private Double actualHours;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Schema(description = "Additional notes about the task")
    private String notes;
}
