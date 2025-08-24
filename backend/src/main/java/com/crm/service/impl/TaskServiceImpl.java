package com.crm.service.impl;

import com.crm.dto.TaskCreateDTO;
import com.crm.dto.TaskDTO;
import com.crm.dto.TaskUpdateDTO;
import com.crm.entity.Customer;
import com.crm.entity.Lead;
import com.crm.entity.Task;
import com.crm.entity.User;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.CustomerRepository;
import com.crm.repository.LeadRepository;
import com.crm.repository.TaskRepository;
import com.crm.repository.UserRepository;
import com.crm.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TaskService interface.
 * Provides business logic for task management operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LeadRepository leadRepository;

    @Override
    public TaskDTO createTask(TaskCreateDTO taskCreateDTO, User createdBy) {
        log.info("Creating new task: {}", taskCreateDTO.getTitle());
        
        Task task = new Task();
        task.setTitle(taskCreateDTO.getTitle());
        task.setDescription(taskCreateDTO.getDescription());
        task.setTaskType(taskCreateDTO.getTaskType());
        task.setPriority(taskCreateDTO.getPriority());
        task.setDueDate(taskCreateDTO.getDueDate());
        task.setStatus(Task.TaskStatus.PENDING);
        task.setCreatedBy(createdBy);
        task.setEstimatedHours(taskCreateDTO.getEstimatedHours());
        task.setNotes(taskCreateDTO.getNotes());

        // Set assigned user if provided
        if (taskCreateDTO.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(taskCreateDTO.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + taskCreateDTO.getAssignedToId()));
            task.setAssignedTo(assignedTo);
        }

        // Set customer if provided
        if (taskCreateDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(taskCreateDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + taskCreateDTO.getCustomerId()));
            task.setCustomer(customer);
        }

        // Set lead if provided
        if (taskCreateDTO.getLeadId() != null) {
            Lead lead = leadRepository.findById(taskCreateDTO.getLeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + taskCreateDTO.getLeadId()));
            task.setLead(lead);
        }

        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());
        
        return convertToDTO(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        log.info("Fetching all tasks");
        return taskRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return convertToDTO(task);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskUpdateDTO taskUpdateDTO) {
        log.info("Updating task with id: {}", id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        // Update fields if provided
        if (taskUpdateDTO.getTitle() != null) {
            task.setTitle(taskUpdateDTO.getTitle());
        }
        if (taskUpdateDTO.getDescription() != null) {
            task.setDescription(taskUpdateDTO.getDescription());
        }
        if (taskUpdateDTO.getTaskType() != null) {
            task.setTaskType(taskUpdateDTO.getTaskType());
        }
        if (taskUpdateDTO.getPriority() != null) {
            task.setPriority(taskUpdateDTO.getPriority());
        }
        if (taskUpdateDTO.getDueDate() != null) {
            task.setDueDate(taskUpdateDTO.getDueDate());
        }
        if (taskUpdateDTO.getStatus() != null) {
            task.setStatus(taskUpdateDTO.getStatus());
            // Set completed date if status is completed
            if (taskUpdateDTO.getStatus() == Task.TaskStatus.COMPLETED && task.getCompletedAt() == null) {
                task.setCompletedAt(LocalDateTime.now());
            }
        }
        if (taskUpdateDTO.getEstimatedHours() != null) {
            task.setEstimatedHours(taskUpdateDTO.getEstimatedHours());
        }
        if (taskUpdateDTO.getActualHours() != null) {
            task.setActualHours(taskUpdateDTO.getActualHours());
        }
        if (taskUpdateDTO.getNotes() != null) {
            task.setNotes(taskUpdateDTO.getNotes());
        }

        // Update assigned user if provided
        if (taskUpdateDTO.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(taskUpdateDTO.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + taskUpdateDTO.getAssignedToId()));
            task.setAssignedTo(assignedTo);
        }

        // Update customer if provided
        if (taskUpdateDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(taskUpdateDTO.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + taskUpdateDTO.getCustomerId()));
            task.setCustomer(customer);
        }

        // Update lead if provided
        if (taskUpdateDTO.getLeadId() != null) {
            Lead lead = leadRepository.findById(taskUpdateDTO.getLeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found with id: " + taskUpdateDTO.getLeadId()));
            task.setLead(lead);
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", updatedTask.getId());
        
        return convertToDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        
        taskRepository.deleteById(id);
        log.info("Task deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByAssignedUser(Long userId) {
        log.info("Fetching tasks assigned to user with id: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return taskRepository.findByAssignedTo(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByCreatedUser(Long userId) {
        log.info("Fetching tasks created by user with id: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return taskRepository.findByCreatedBy(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByStatus(Task.TaskStatus status) {
        log.info("Fetching tasks with status: {}", status);
        return taskRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByPriority(Task.TaskPriority priority) {
        log.info("Fetching tasks with priority: {}", priority);
        return taskRepository.findByPriority(priority).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByType(Task.TaskType taskType) {
        log.info("Fetching tasks with type: {}", taskType);
        return taskRepository.findByTaskType(taskType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getOverdueTasks() {
        log.info("Fetching overdue tasks");
        return taskRepository.findOverdueTasks(LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getOverdueTasksByUser(Long userId) {
        log.info("Fetching overdue tasks for user with id: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return taskRepository.findOverdueTasksByUser(user, LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByDueDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching tasks due between {} and {}", startDate, endDate);
        return taskRepository.findByDueDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByCustomer(Long customerId) {
        log.info("Fetching tasks for customer with id: {}", customerId);
        return taskRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByLead(Long leadId) {
        log.info("Fetching tasks for lead with id: {}", leadId);
        return taskRepository.findByLeadId(leadId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> searchTasks(String searchTerm) {
        log.info("Searching tasks with term: {}", searchTerm);
        return taskRepository.findByTitleOrDescriptionContainingIgnoreCase(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTaskStatus(Long id, Task.TaskStatus status) {
        log.info("Updating task status to {} for task with id: {}", status, id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        task.setStatus(status);
        
        // Set completed date if status is completed
        if (status == Task.TaskStatus.COMPLETED && task.getCompletedAt() == null) {
            task.setCompletedAt(LocalDateTime.now());
        }
        
        Task updatedTask = taskRepository.save(task);
        log.info("Task status updated successfully to {} for task with id: {}", status, id);
        
        return convertToDTO(updatedTask);
    }

    @Override
    public TaskDTO assignTask(Long id, Long userId) {
        log.info("Assigning task with id: {} to user with id: {}", id, userId);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        task.setAssignedTo(user);
        Task updatedTask = taskRepository.save(task);
        log.info("Task assigned successfully to user: {} for task with id: {}", user.getUsername(), id);
        
        return convertToDTO(updatedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStatistics getTaskStatistics(Long userId) {
        log.info("Fetching task statistics for user with id: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        TaskStatistics stats = new TaskStatistics(userId, user.getUsername());
        stats.setTotalTasks(taskRepository.countByAssignedTo(user));
        stats.setPendingTasks(taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.PENDING));
        stats.setInProgressTasks(taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.IN_PROGRESS));
        stats.setCompletedTasks(taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.COMPLETED));
        stats.setOverdueTasks(taskRepository.countOverdueTasksByUser(user, LocalDateTime.now()));
        
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatistics> getAllTaskStatistics() {
        log.info("Fetching task statistics for all users");
        
        return userRepository.findAll().stream()
                .map(user -> {
                    TaskStatistics stats = new TaskStatistics(user.getId(), user.getUsername());
                    stats.setTotalTasks(taskRepository.countByAssignedTo(user));
                    stats.setPendingTasks(taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.PENDING));
                    stats.setInProgressTasks(taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.IN_PROGRESS));
                    stats.setCompletedTasks(taskRepository.countByAssignedToAndStatus(user, Task.TaskStatus.COMPLETED));
                    stats.setOverdueTasks(taskRepository.countOverdueTasksByUser(user, LocalDateTime.now()));
                    return stats;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convert Task entity to TaskDTO.
     *
     * @param task the task entity
     * @return the task DTO
     */
    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setTaskType(task.getTaskType());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        dto.setEstimatedHours(task.getEstimatedHours());
        dto.setActualHours(task.getActualHours());
        dto.setNotes(task.getNotes());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setCompletedAt(task.getCompletedAt());

        // Set assigned user info
        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getId());
            dto.setAssignedToName(task.getAssignedTo().getUsername());
        }

        // Set customer info
        if (task.getCustomer() != null) {
            dto.setCustomerId(task.getCustomer().getId());
            dto.setCustomerName(task.getCustomer().getCompanyName());
        }

        // Set lead info
        if (task.getLead() != null) {
            dto.setLeadId(task.getLead().getId());
            dto.setLeadTitle(task.getLead().getTitle());
        }

        // Set created by info
        if (task.getCreatedBy() != null) {
            dto.setCreatedById(task.getCreatedBy().getId());
            dto.setCreatedByName(task.getCreatedBy().getUsername());
        }

        // Check if task is overdue
        dto.setOverdue(task.getDueDate().isBefore(LocalDate.now()) && 
                      task.getStatus() != Task.TaskStatus.COMPLETED && 
                      task.getStatus() != Task.TaskStatus.CANCELLED);

        return dto;
    }
}
