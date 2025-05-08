// model/Task.java
package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a task in the todo list
 */
public class Task implements Serializable {
    private int taskId;
    private String userId;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private PriorityLevel priority;

    public Task(int taskId, String userId, String title, String description,
                LocalDateTime dueDate, TaskStatus status, LocalDateTime createdAt,
                PriorityLevel priority) {
        this.taskId = taskId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = createdAt;
        this.priority = priority;
    }

    // Getters and setters
    public int getTaskId() { return taskId; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public PriorityLevel getPriority() { return priority; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setPriority(PriorityLevel priority) { this.priority = priority; }
}

