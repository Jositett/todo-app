// tests/CSVFileManagerTest.java
package tests;

import model.*;
import utils.CSVFileManager;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CSVFileManagerTest {
    private static final String TEST_USERS_FILE = "test_users.csv";
    private static final String TEST_TASKS_FILE = "test_tasks.csv";
    private static final String TEST_EXPORT_FILE = "test_export.csv";
    private static final String TEST_IMPORT_FILE = "test_import.csv";

    @Before
    public void setUp() throws Exception {
        // Clean up test files
        deleteTestFiles();
    }

    @After
    public void tearDown() throws Exception {
        // Clean up test files
        deleteTestFiles();
    }

    private void deleteTestFiles() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_USERS_FILE));
        Files.deleteIfExists(Paths.get(TEST_TASKS_FILE));
        Files.deleteIfExists(Paths.get(TEST_EXPORT_FILE));
        Files.deleteIfExists(Paths.get(TEST_IMPORT_FILE));
    }

    @Test
    public void testUserFileOperations() throws Exception {
        // Test user creation
        User user = new User("testuser", "testhash");

        // Test saving user
        CSVFileManager.saveUser(user);

        // Test loading user
        List<User> users = CSVFileManager.loadUsers();
        assertNotNull("User list should not be null", users);
        assertEquals("Should have 1 user", 1, users.size());
        assertEquals("Username should match", user.getUsername(), users.getFirst().getUsername());
        assertEquals("Hash should match", user.getHashedPassword(), users.getFirst().getHashedPassword());
    }

    @Test
    public void testTaskFileOperations() throws Exception {
        // Create test task
        Task task = new Task(1, "testuser", "Test Task", "Test Description",
                LocalDateTime.now().plusDays(1), TaskStatus.INCOMPLETE,
                LocalDateTime.now(), PriorityLevel.HIGH);

        // Test saving task
        CSVFileManager.saveTask(task);

        // Test loading task
        List<Task> tasks = CSVFileManager.loadTasks();
        assertNotNull("Task list should not be null", tasks);
        assertEquals("Should have 1 task", 1, tasks.size());

        Task loadedTask = tasks.getFirst();
        assertEquals("Task ID should match", task.getTaskId(), loadedTask.getTaskId());
        assertEquals("User ID should match", task.getUserId(), loadedTask.getUserId());
        assertEquals("Title should match", task.getTitle(), loadedTask.getTitle());
        assertEquals("Description should match", task.getDescription(), loadedTask.getDescription());
        assertEquals("Due date should match", task.getDueDate(), loadedTask.getDueDate());
        assertEquals("Status should match", task.getStatus(), loadedTask.getStatus());
        assertEquals("Priority should match", task.getPriority(), loadedTask.getPriority());
    }

    @Test
    public void testBulkOperations() throws Exception {
        // Create test tasks
        Task task1 = new Task(1, "testuser", "Task 1", "",
                LocalDateTime.now().plusDays(1), TaskStatus.INCOMPLETE,
                LocalDateTime.now(), PriorityLevel.LOW);
        Task task2 = new Task(2, "testuser", "Task 2", "",
                LocalDateTime.now().plusDays(2), TaskStatus.COMPLETE,
                LocalDateTime.now(), PriorityLevel.HIGH);

        // Test saving multiple tasks
        CSVFileManager.saveTasks(Arrays.asList(task1, task2));

        // Test loading multiple tasks
        List<Task> tasks = CSVFileManager.loadTasks();
        assertNotNull("Task list should not be null", tasks);
        assertEquals("Should have 2 tasks", 2, tasks.size());

        // Test export
        CSVFileManager.exportTasks(Arrays.asList(task1, task2), TEST_EXPORT_FILE, true, true, true);

        // Test file exists
        assertTrue("Export file should exist", Files.exists(Paths.get(TEST_EXPORT_FILE)));

        // Test import
        List<Task> importedTasks = CSVFileManager.importTasks(TEST_IMPORT_FILE, "testuser");
        assertNotNull("Imported tasks should not be null", importedTasks);
        assertFalse("Imported tasks should not be empty", importedTasks.isEmpty());
    }

    @Test
    public void testFileValidation() throws Exception {
        // Test invalid file
        try {
            CSVFileManager.loadUsers();
            CSVFileManager.loadTasks();
        } catch (Exception e) {
            fail("Should not throw exception when files don't exist");
        }

        // Test malformed file
        // Create a file with invalid data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_TASKS_FILE))) {
            writer.write("invalid,data");
        }

        List<Task> tasks = CSVFileManager.loadTasks();
        assertNotNull("Task list should not be null", tasks);
        assertTrue("Task list should be empty", tasks.isEmpty());
    }
}