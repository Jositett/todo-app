// controller/TaskController.java
package controller;

import model.*;
import view.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller for the task view
 */
public class TaskController {
    // In TaskController.java
    private final TaskView view;
    private final TaskManager taskManager;

    public TaskController(TaskView view, TaskManager taskManager) {
        this.view = view;
        this.taskManager = taskManager;
    }

    /**
     * Loads tasks from the task manager
     */
    public void loadTasks() {
        try {
            taskManager.loadUserTasks();
            view.setTasks(taskManager.getAllTasks());
        } catch (Exception e) {
            view.showErrorMessage("Error loading tasks: " + e.getMessage());
        }
    }

    /**
     * Handles filter change
     */
    public void handleFilterChange() {
        String filter = view.getFilterSelection();

        try {
            List<Task> filteredTasks;

            switch (filter) {
                case "Complete":
                    filteredTasks = taskManager.filterTasksByStatus(TaskStatus.COMPLETE);
                    break;
                case "Incomplete":
                    filteredTasks = taskManager.filterTasksByStatus(TaskStatus.INCOMPLETE);
                    break;
                case "High Priority":
                    filteredTasks = taskManager.filterTasksByPriority(PriorityLevel.URGENT);
                    break;
                default:
                    filteredTasks = taskManager.getAllTasks();
            }

            view.setTasks(filteredTasks);
        } catch (Exception e) {
            view.showErrorMessage("Error applying filter: " + e.getMessage());
        }
    }

    /**
     * Handles sort change
     */
    public void handleSortChange() {
        String sort = view.getSortSelection();

        try {
            List<Task> sortedTasks;

            switch (sort) {
                case "Due Date":
                    sortedTasks = taskManager.sortTasksByDueDate();
                    break;
                case "Priority":
                    sortedTasks = taskManager.sortTasksByPriority();
                    break;
                default:
                    sortedTasks = taskManager.getAllTasks();
            }

            view.setTasks(sortedTasks);
        } catch (Exception e) {
            view.showErrorMessage("Error applying sort: " + e.getMessage());
        }
    }

    /**
     * Handles add task button click
     */
    public void handleAddTask() {
        view.showTaskDialog(new Task(0,
                taskManager.getCurrentUser().getUsername(),
                "", "",
                null, TaskStatus.INCOMPLETE,
                LocalDateTime.now(),
                PriorityLevel.MEDIUM), true);
    }

    /**
     * Handles edit task button click
     */
    public void handleEditTask() {
        Task selectedTask = view.getSelectedTask();
        if (selectedTask != null) {
            view.showTaskDialog(selectedTask, false);
        } else {
            view.showErrorMessage("Please select a task to edit.");
        }
    }

    /**
     * Handles delete task button click
     */
    public void handleDeleteTask() {
        Task selectedTask = view.getSelectedTask();
        if (selectedTask != null) {
            int result = view.showConfirmDialog("Are you sure you want to delete this task?");
            if (result == JOptionPane.YES_OPTION) {
                try {
                    if (taskManager.deleteTask(selectedTask)) {
                        loadTasks();
                    }
                } catch (Exception e) {
                    view.showErrorMessage("Error deleting task: " + e.getMessage());
                }
            }
        } else {
            view.showErrorMessage("Please select a task to delete.");
        }
    }

    /**
     * Handles adding a new task
     */
    public void handleAddTask(Task task) {
        try {
            // Basic validation
            if (task.getTitle().trim().isEmpty()) {
                view.showErrorMessage("Task title cannot be empty.");
                return;
            }

            taskManager.createTask(
                    task.getTitle(),
                    task.getDescription(),
                    task.getDueDate(),
                    task.getPriority()
            );

            loadTasks();
        } catch (Exception e) {
            view.showErrorMessage("Error adding task: " + e.getMessage());
        }
    }

    /**
     * Handles updating an existing task
     */
    public void handleUpdateTask(Task task) {
        try {
            // Basic validation
            if (task.getTitle().trim().isEmpty()) {
                view.showErrorMessage("Task title cannot be empty.");
                return;
            }

            taskManager.updateTask(task);
            loadTasks();
        } catch (Exception e) {
            view.showErrorMessage("Error updating task: " + e.getMessage());
        }
    }

    /**
     * Handles export menu item click
     */
    public void handleExport() {
        view.showExportDialog();
    }

    /**
     * Handles export tasks
     */
    public void handleExportTasks(String filename, boolean includeDescription,
                                  boolean includeDueDate, boolean includePriority) {
        try {
            taskManager.exportTasks(filename, includeDescription, includeDueDate, includePriority);
            view.showSuccessMessage("Tasks exported successfully to " + filename);
        } catch (Exception e) {
            view.showErrorMessage("Error exporting tasks: " + e.getMessage());
        }
    }

    /**
     * Handles import menu item click
     */
    public void handleImport() {
        view.showImportDialog();
    }

    /**
     * Handles importing tasks
     */
    public void handleImportTasks(String filename) {
        try {
            List<Task> importedTasks = taskManager.importTasks(filename);
            view.setTasks(importedTasks);
            view.showSuccessMessage("Imported " + importedTasks.size() + " tasks from " + filename);
        } catch (Exception e) {
            view.showErrorMessage("Error importing tasks: " + e.getMessage());
        }
    }

    /**
     * Handles logout menu item click
     */
    public void handleLogout() {
        int result = view.showConfirmDialog("Are you sure you want to log out?");
        if (result == JOptionPane.YES_OPTION) {
            taskManager.logout();
            view.dispose();

            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        }
    }

    /**
     * Handles toggle theme menu item click
     */
    public void handleToggleTheme() {
        int currentTheme = ThemeManager.getCurrentTheme();
        int newTheme = (currentTheme == ThemeManager.LIGHT_THEME) ?
                ThemeManager.DARK_THEME : ThemeManager.LIGHT_THEME;

        ThemeManager.applyTheme(newTheme);
    }
}