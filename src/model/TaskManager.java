// model/TaskManager.java
package model;

import java.time.LocalDateTime;
import java.util.*;

import org.mindrot.jbcrypt.BCrypt;
import utils.CSVFileManager;

/**
 * Manages tasks and user authentication
 */
public class TaskManager {
    private User currentUser;
    private final List<Task> tasks;
//    private final CSVFileManager fileManager;

    public TaskManager() {
//        fileManager = new CSVFileManager();
        tasks = new ArrayList<>();
    }

    /**
     * Authenticates a user
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return true if authentication succeeds, false otherwise
     */
    public boolean login(String username, String password) throws Exception {
        List<User> users = CSVFileManager.loadUsers();

        for (User user : users) {
            if (user.getHashedPassword().equals(BCrypt.hashpw(password,
                    user.getHashedPassword())) && user.getUsername().equals(username)) {
                currentUser = user;
                loadUserTasks();
                return true;
            }
        }

        return false;
    }

    /**
     * Registers a new user
     * @param username The new username
     * @param password The new password
     * @return true if registration succeeds, false otherwise
     */
    public boolean register(String username, String password) throws Exception {
        // Check if username already exists
        List<User> users = CSVFileManager.loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // Username already taken
            }
        }

        // Create new user
        User newUser = new User(username, hashPassword(password));
        CSVFileManager.saveUser(newUser);
        currentUser = newUser;
        return true;
    }

    /**
     * Creates a hash of the password
     */
    private String hashPassword(String password) {
        // Generate a secure salt and hash the password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Creates a new task
     */
    public Task createTask(String title, String description, LocalDateTime dueDate,
                           PriorityLevel priority) {
        int taskId = generateTaskId();
        LocalDateTime createdAt = LocalDateTime.now();
        Task task = new Task(taskId, currentUser.getUsername(), title, description,
                dueDate, TaskStatus.INCOMPLETE, createdAt, priority);

        try {
            CSVFileManager.saveTask(task);
            tasks.add(task);
            return task;
        } catch (Exception e) {
            System.err.println("Error saving task: " + e.getMessage());
            return null;
        }
    }

    /**
     * Loads all tasks for the current user
     */
    public void loadUserTasks() throws Exception {
        tasks.clear();
        List<Task> allTasks = CSVFileManager.loadTasks();

        for (Task task : allTasks) {
            if (task.getUserId().equals(currentUser.getUsername())) {
                tasks.add(task);
            }
        }
    }

    /**
     * Generates a unique task ID
     */
    private int generateTaskId() {
        int maxId = 0;

        for (Task task : tasks) {
            if (task.getTaskId() > maxId) {
                maxId = task.getTaskId();
            }
        }

        return maxId + 1;
    }

    /**
     * Updates an existing task
     */
    public boolean updateTask(Task task) {
        try {
            // In a real application, we would update the file directly
            // For simplicity, we'll just reload all tasks
            saveAllTasks();
            return true;
        } catch (Exception e) {
            System.err.println("Error updating task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a task
     */
    public boolean deleteTask(Task task) {
        tasks.remove(task);
        try {
            saveAllTasks();
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves all tasks to file
     */
    private void saveAllTasks() throws Exception {
        CSVFileManager.saveTasks(tasks);
    }

    /**
     * Exports tasks to CSV
     */
    public void exportTasks(String filename, boolean includeDescription,
                            boolean includeDueDate, boolean includePriority) throws Exception {
        CSVFileManager.exportTasks(tasks, filename, includeDescription, includeDueDate, includePriority);
    }

    /**
     * Imports tasks from CSV
     */
    public List<Task> importTasks(String filename) throws Exception {
        List<Task> importedTasks = CSVFileManager.importTasks(filename, currentUser.getUsername());

        // Add imported tasks to our list and save
        for (Task task : importedTasks) {
            if (!tasks.contains(task)) {
                tasks.add(task);
            }
        }

        saveAllTasks();
        return importedTasks;
    }

    /**
     * Filters tasks by completion status
     */
    public List<Task> filterTasksByStatus(TaskStatus status) {
        List<Task> filteredTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getStatus() == status) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    /**
     * Filters tasks by priority
     */
    public List<Task> filterTasksByPriority(PriorityLevel priority) {
        List<Task> filteredTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getPriority() == priority) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    /**
     * Sorts tasks by due date
     */
    public List<Task> sortTasksByDueDate() {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparing(Task::getDueDate));
        return sortedTasks;
    }

    /**
     * Sorts tasks by priority
     */
    public List<Task> sortTasksByPriority() {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        // Define priority order
        Map<PriorityLevel, Integer> priorityOrder = new HashMap<>();
        priorityOrder.put(PriorityLevel.LOW, 0);
        priorityOrder.put(PriorityLevel.MEDIUM, 1);
        priorityOrder.put(PriorityLevel.HIGH, 2);
        priorityOrder.put(PriorityLevel.URGENT, 3);

        sortedTasks.sort((t1, t2) ->
                Integer.compare(priorityOrder.get(t2.getPriority()),
                        priorityOrder.get(t1.getPriority())));

        return sortedTasks;
    }

    /**
     * Gets all tasks
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Gets the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        currentUser = null;
        tasks.clear();
    }
}