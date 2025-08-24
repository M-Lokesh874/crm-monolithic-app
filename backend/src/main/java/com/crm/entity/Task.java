package com.crm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * Task entity representing tasks and activities in the CRM system.
 * Tasks can be assigned to users and linked to customers or leads.
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task entity for managing CRM activities and assignments")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the task", example = "1")
    private Long id;

    @NotBlank(message = "Task title is required")
    @Size(min = 3, max = 255, message = "Task title must be between 3 and 255 characters")
    @Column(name = "title", nullable = false)
    @Schema(description = "Title of the task", example = "Follow up with TechCorp proposal")
    private String title;

    @Size(max = 1000, message = "Task description cannot exceed 1000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Detailed description of the task", example = "Send updated proposal and schedule demo call")
    private String description;

    @NotNull(message = "Task type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    @Schema(description = "Type of task", example = "FOLLOW_UP")
    private TaskType taskType;

    @NotNull(message = "Task priority is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    @Schema(description = "Priority level of the task", example = "HIGH")
    private TaskPriority priority;

    @NotNull(message = "Due date is required")
    @Column(name = "due_date", nullable = false)
    @Schema(description = "Due date for the task", example = "2024-01-15")
    private LocalDate dueDate;

    @NotNull(message = "Task status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false)
    @Schema(description = "Current status of the task", example = "IN_PROGRESS")
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    @Schema(description = "User assigned to the task")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @Schema(description = "Customer associated with the task")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    @Schema(description = "Lead associated with the task")
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @Schema(description = "User who created the task")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Timestamp when the task was created")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the task was last updated")
    private LocalDateTime updatedAt;

    @Column(name = "completed_date")
    @Schema(description = "Timestamp when the task was completed")
    private LocalDateTime completedAt;

    @Column(name = "estimated_hours")
    @Schema(description = "Estimated hours to complete the task", example = "2.5")
    private Double estimatedHours;

    @Column(name = "actual_hours")
    @Schema(description = "Actual hours spent on the task", example = "3.0")
    private Double actualHours;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Column(name = "notes", columnDefinition = "TEXT")
    @Schema(description = "Additional notes about the task")
    private String notes;

    /**
     * Task types available in the system
     */
    public enum TaskType {
        FOLLOW_UP("Follow Up"),
        COLD_CALL("Cold Call"),
        REPORT("Report"),
        MEETING("Meeting"),
        PRESENTATION("Presentation"),
        PROPOSAL("Proposal"),
        NEGOTIATION("Negotiation"),
        CLOSING("Closing"),
        SUPPORT("Support"),
        TRAINING("Training"),
        OTHER("Other");

        private final String displayName;

        TaskType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Task priority levels
     */
    public enum TaskPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        URGENT("Urgent");

        private final String displayName;

        TaskPriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Task status values
     */
    public enum TaskStatus {
        PENDING("Pending"),
        IN_PROGRESS("In Progress"),
        ON_HOLD("On Hold"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled"),
        OVERDUE("Overdue");

        private final String displayName;

        TaskStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
