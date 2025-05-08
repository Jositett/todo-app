// tests/TaskManagerTest.java
package tests;

import model.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagerTest {
    private TaskManager taskManager;
    private static final String TEST_USER = "testuser";
    private static final String TEST_PASSWORD = "testpassword";

    @Before
    public void setUp() throws Exception {
        // Create a new task manager
        taskManager = new TaskManager();

        // Register a test user
        taskManager.register(TEST_USER, TEST_PASSWORD);

        // Login with the test user
        taskManager.login(TEST_USER, TEST_PASSWORD);
    }

    @Test
    public void testTaskCRUDOperations() throws Exception {
        // Test task creation
        LocalDateTime now = LocalDateTime.now();
        Task testTask = taskManager.createTask("Test Task", "Test Description",
                now.plusDays(1), PriorityLevel.HIGH);

        assertNotNull("Task should be created", testTask);
        assertEquals("Task title should match", "Test Task", testTask.getTitle());
        assertEquals("Task description should match", "Test Description", testTask.getDescription());
        assertEquals("Priority should match", PriorityLevel.HIGH, testTask.getPriority());

        // Verify the task was saved
        List<Task> tasks = taskManager.getAllTasks();
        assertTrue("Task list should contain new task", tasks.contains(testTask));

        // Test task update
        testTask.setTitle("Updated Title");
        testTask.setDescription("Updated Description");
        testTask.setPriority(PriorityLevel.URGENT);
        testTask.setDueDate(now.plusDays(2));
        testTask.setStatus(TaskStatus.COMPLETE);

        assertTrue("Task should be updated", taskManager.updateTask(testTask));

        // Verify the update
        tasks = taskManager.getAllTasks();
        Task updatedTask = tasks.get(tasks.size() - 1);
        assertEquals("Updated title should match", "Updated Title", updatedTask.getTitle());
        assertEquals("Updated description should match", "Updated Description", updatedTask.getDescription());
        assertEquals("Updated priority should match", PriorityLevel.URGENT, updatedTask.getPriority());
        assertEquals("Updated status should match", TaskStatus.COMPLETE, updatedTask.getStatus());

        // Test task deletion
        assertTrue("Task should be deleted", taskManager.deleteTask(updatedTask));

        // Verify the deletion
        tasks = taskManager.getAllTasks();
        assertFalse("Task list should not contain deleted task", tasks.contains(updatedTask));
    }

    @Test
    public void testUserAuthentication() throws Exception {
        // Test successful login
        assertTrue("Login should succeed", taskManager.login(TEST_USER, TEST_PASSWORD));

        // Test invalid password
        assertFalse("Login should fail with invalid password",
                taskManager.login(TEST_USER, "wrongpassword"));

        // Test invalid username
        assertFalse("Login should fail with invalid username",
                taskManager.login("wronguser", TEST_PASSWORD));
    }

    @Test
    public void testTaskFiltering() throws Exception {
        // Create test tasks
        taskManager.createTask("Task 1", "", LocalDateTime.now().plusDays(1), PriorityLevel.LOW);
        taskManager.createTask("Task 2", "", LocalDateTime.now().plusDays(2), PriorityLevel.URGENT);
        taskManager.createTask("Task 3", "", LocalDateTime.now().plusDays(3), PriorityLevel.HIGH);

        // Test status filtering
        List<Task> completeTasks = taskManager.filterTasksByStatus(TaskStatus.COMPLETE);
        assertTrue("Initially no complete tasks", completeTasks.isEmpty());

        List<Task> incompleteTasks = taskManager.filterTasksByStatus(TaskStatus.INCOMPLETE);
        assertTrue("Should have at least 3 incomplete tasks", incompleteTasks.size() >= 3);

        // Test priority filtering
        List<Task> highPriorityTasks = taskManager.filterTasksByPriority(PriorityLevel.URGENT);
        assertTrue("Should have at least 1 urgent task", highPriorityTasks.size() >= 1);
    }

    @Test
    public void testTaskSorting() throws Exception {
        // Create test tasks with different due dates
        LocalDateTime now = LocalDateTime.now();
        taskManager.createTask("Task A", "", now.plusDays(3), PriorityLevel.MEDIUM);
        taskManager.createTask("Task B", "", now.plusDays(1), PriorityLevel.HIGH);
        taskManager.createTask("Task C", "", now.plusDays(2), PriorityLevel.LOW);
        taskManager.createTask("Task D", "", now.plusDays(5), PriorityLevel.URGENT);
        taskManager.createTask("Task E", "", now.plusDays(4), PriorityLevel.MEDIUM);

        // Test sorting by due date
        List<Task> sortedByDueDate = taskManager.sortTasksByDueDate();
        LocalDateTime lastDate = null;

        for (Task task : sortedByDueDate) {
            if (lastDate != null) {
                assertTrue("Tasks should be sorted by due date",
                        task.getDueDate().isAfter(lastDate) ||
                                task.getDueDate().isEqual(lastDate));
            }
            lastDate = task.getDueDate();
        }

        // Test sorting by priority
        List<Task> sortedByPriority = taskManager.sortTasksByPriority();
        PriorityLevel lastPriority = null;
        Map<PriorityLevel, Integer> priorityOrder = new HashMap<>();
        priorityOrder.put(PriorityLevel.LOW, 0);
        priorityOrder.put(PriorityLevel.MEDIUM, 1);
        priorityOrder.put(PriorityLevel.HIGH, 2);
        priorityOrder.put(PriorityLevel.URGENT, 3);

        for (Task task : sortedByPriority) {
            if (lastPriority != null) {
                assertTrue("Tasks should be sorted by priority",
                        priorityOrder.get(task.getPriority()) >=
                                priorityOrder.get(lastPriority));
            }
            lastPriority = task.getPriority();
        }
    }

    @Test
    public void testTaskValidation() throws Exception {
        // Test empty title
        try {
            taskManager.createTask("", "Test Description", LocalDateTime.now().plusDays(1), PriorityLevel.MEDIUM);
            fail("Should not allow task with empty title");
        } catch (Exception e) {
            // Expected exception
        }

        // Test null due date is allowed
        Task taskWithNullDueDate = taskManager.createTask("Test Task", "Test Description", null, PriorityLevel.MEDIUM);
        assertNotNull("Task should be created", taskWithNullDueDate);
        assertNull("Due date should be null", taskWithNullDueDate.getDueDate());
    }
}