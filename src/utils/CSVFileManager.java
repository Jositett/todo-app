// utils/CSVFileManager.java
package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDateTime;
import model.*;

/**
 * Handles file operations for user and task data
 */
public class CSVFileManager {
    private static final String USERS_FILE = "users.csv";
    private static final String TASKS_FILE = "tasks.csv";

    /**
     * Saves a user to the users file
     */
    public static void saveUser(User user) throws IOException {
        // Create users file if it doesn't exist
        if (!Files.exists(Paths.get(USERS_FILE))) {
            createNewFileWithHeader(USERS_FILE, "username,password");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(user.getUsername() + "," + user.getHashedPassword());
            writer.newLine();
        }
    }

    /**
     * Loads all users from the users file
     */
    public static List<User> loadUsers() throws IOException {
        List<User> users = new ArrayList<>();

        if (!Files.exists(Paths.get(USERS_FILE))) {
            return users; // Return empty list if file doesn't exist
        }

        List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));

        // Skip header line
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length == 2) {
                users.add(new User(parts[0], parts[1]));
            }
        }

        return users;
    }

    /**
     * Saves a task to the tasks file
     */
    public static void saveTask(Task task) throws IOException {
        // Create tasks file if it doesn't exist
        if (!Files.exists(Paths.get(TASKS_FILE))) {
            createNewFileWithHeader(TASKS_FILE, "task_id,user_id,title,description,due_date,status,created_at,priority");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {
            writer.write(task.getTaskId() + "," +
                    task.getUserId() + "," +
                    task.getTitle() + "," +
                    task.getDescription() + "," +
                    task.getDueDate() + "," +
                    task.getStatus() + "," +
                    task.getCreatedAt() + "," +
                    task.getPriority());
            writer.newLine();
        }
    }

    /**
     * Loads all tasks from the tasks file
     */
    public static List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(Paths.get(TASKS_FILE))) {
            return tasks; // Return empty list if file doesn't exist
        }

        List<String> lines = Files.readAllLines(Paths.get(TASKS_FILE));

        // Skip header line
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length == 8) {
                try {
                    int taskId = Integer.parseInt(parts[0]);
                    String userId = parts[1];
                    String title = parts[2];
                    String description = parts[3];
                    LocalDateTime dueDate = LocalDateTime.parse(parts[4]);
                    TaskStatus status = TaskStatus.valueOf(parts[5]);
                    LocalDateTime createdAt = LocalDateTime.parse(parts[6]);
                    PriorityLevel priority = PriorityLevel.valueOf(parts[7]);

                    tasks.add(new Task(taskId, userId, title, description, dueDate, status, createdAt, priority));
                } catch (Exception e) {
                    // Skip invalid line but log error
                    System.err.println("Error parsing task data: " + lines.get(i));
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        return tasks;
    }

    /**
     * Creates a new CSV file with the specified header
     */
    private static void createNewFileWithHeader(String filename, String header) throws IOException {
        Files.createFile(Paths.get(filename));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(header);
            writer.newLine();
        }
    }

    /**
     * Saves multiple tasks to the tasks file
     */
    public static void saveTasks(List<Task> tasks) throws IOException {
        // Overwrite existing file
        boolean append = false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE, append))) {
            writer.write("task_id,user_id,title,description,due_date,status,created_at,priority");
            writer.newLine();

            for (Task task : tasks) {
                writer.write(task.getTaskId() + "," +
                        task.getUserId() + "," +
                        task.getTitle() + "," +
                        task.getDescription() + "," +
                        task.getDueDate() + "," +
                        task.getStatus() + "," +
                        task.getCreatedAt() + "," +
                        task.getPriority());
                writer.newLine();
            }
        }
    }

    /**
     * Exports selected tasks to a CSV file
     */
    public static void exportTasks(List<Task> tasks, String filename, boolean includeDescription,
                                   boolean includeDueDate, boolean includePriority) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Build header based on selected fields
            StringBuilder header = new StringBuilder("Title");
            if (includeDescription) header.append(",Description");
            if (includeDueDate) header.append(",Due Date");
            if (includePriority) header.append(",Priority");

            writer.write(header.toString());
            writer.newLine();

            // Build each task line based on selected fields
            for (Task task : tasks) {
                StringBuilder line = new StringBuilder(task.getTitle());
                if (includeDescription) line.append(",").append(task.getDescription());
                if (includeDueDate) line.append(",").append(task.getDueDate());
                if (includePriority) line.append(",").append(task.getPriority());

                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Imports tasks from a CSV file
     */
    public static List<Task> importTasks(String filename, String userId) throws IOException {
        List<Task> importedTasks = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filename));

        // Assume first line is header
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");

            if (parts.length >= 1) { // At least title is required
                try {
                    String title = parts[0];
                    String description = (parts.length > 1) ? parts[1] : "";
                    LocalDateTime dueDate = (parts.length > 2) ? LocalDateTime.parse(parts[2]) : null;
                    PriorityLevel priority = (parts.length > 3) ? PriorityLevel.valueOf(parts[3].toUpperCase()) : PriorityLevel.MEDIUM;

                    int taskId = generateTaskId();
                    LocalDateTime createdAt = LocalDateTime.now();
                    Task task = new Task(taskId, userId, title, description,
                            dueDate, TaskStatus.INCOMPLETE, createdAt, priority);

                    importedTasks.add(task);
                } catch (Exception e) {
                    // Skip invalid line but log error
                    System.err.println("Error parsing imported task data: " + lines.get(i));
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        return importedTasks;
    }

    /**
     * Generates a unique task ID
     */
    private static int generateTaskId() throws IOException {
        List<Task> existingTasks = loadTasks();
        int maxId = 0;

        for (Task task : existingTasks) {
            if (task.getTaskId() > maxId) {
                maxId = task.getTaskId();
            }
        }

        return maxId + 1;
    }
}