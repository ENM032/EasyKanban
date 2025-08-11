package com.mycompany.easykanban;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.sql.SQLException;

/**
 * Comprehensive tests for DatabaseManager functionality
 * Tests password hashing, user management, and task operations
 */
public class DatabaseManagerTest {
    
    private DatabaseManager dbManager;
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "Test@123";
    private static final String TEST_FIRSTNAME = "John";
    private static final String TEST_LASTNAME = "Doe";
    
    @BeforeEach
    public void setUp() {
        dbManager = DatabaseManager.getInstance();
        // Clean up any existing test data
        try {
            dbManager.deleteUser(TEST_USERNAME);
        } catch (Exception e) {
            // Ignore if user doesn't exist
        }
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test data
        try {
            dbManager.deleteUser(TEST_USERNAME);
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }
    
    // Password Hashing Tests
    @Test
    public void testPasswordHashing() {
        String hashedPassword = dbManager.hashPassword(TEST_PASSWORD);
        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertNotEquals(TEST_PASSWORD, hashedPassword, "Hashed password should differ from original");
        assertTrue(hashedPassword.startsWith("$2a$"), "Should use BCrypt format");
    }
    
    @Test
    public void testPasswordVerification() {
        String hashedPassword = dbManager.hashPassword(TEST_PASSWORD);
        assertTrue(dbManager.verifyPassword(TEST_PASSWORD, hashedPassword), 
                  "Password verification should succeed with correct password");
        assertFalse(dbManager.verifyPassword("wrongPassword", hashedPassword), 
                   "Password verification should fail with incorrect password");
    }
    
    // User Management Tests
    @Test
    public void testUserRegistration() {
        boolean result = dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        assertTrue(result, "User registration should succeed");
        
        // Test duplicate registration
        boolean duplicateResult = dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        assertFalse(duplicateResult, "Duplicate user registration should fail");
    }
    
    @Test
    public void testUserAuthentication() {
        // Register user first
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        
        // Test successful authentication
        DatabaseManager.User user = dbManager.authenticateUser(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(user, "Authentication should succeed with correct credentials");
        assertEquals(TEST_USERNAME, user.getUsername(), "Username should match");
        assertEquals(TEST_FIRSTNAME, user.getFirstName(), "First name should match");
        assertEquals(TEST_LASTNAME, user.getLastName(), "Last name should match");
        
        // Test failed authentication
        DatabaseManager.User failedUser = dbManager.authenticateUser(TEST_USERNAME, "wrongPassword");
        assertNull(failedUser, "Authentication should fail with incorrect password");
        
        DatabaseManager.User nonExistentUser = dbManager.authenticateUser("nonExistent", TEST_PASSWORD);
        assertNull(nonExistentUser, "Authentication should fail for non-existent user");
    }
    
    // Task Management Tests
    @Test
    public void testTaskCreation() {
        // Register user first
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        
        boolean result = dbManager.createTask(
            "Test Task", "Test Description", 
            "John Developer", 5, "To Do", "TST:0:DEV", 0, TEST_USERNAME
        );
        assertTrue(result, "Task creation should succeed");
    }
    
    @Test
    public void testGetAllTasks() {
        // Register user and create tasks
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        dbManager.createTask("Task 1", "Description 1", "Dev 1", 3, "To Do", "T1:0:DEV", 0, TEST_USERNAME);
        dbManager.createTask("Task 2", "Description 2", "Dev 2", 5, "Done", "T2:1:DEV", 1, TEST_USERNAME);
        
        List<DatabaseManager.Task> tasks = dbManager.getAllTasks(TEST_USERNAME);
        assertEquals(2, tasks.size(), "Should retrieve all user tasks");
    }
    
    @Test
    public void testGetTasksByStatus() {
        // Register user and create tasks
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        dbManager.createTask("Task 1", "Description 1", "Dev 1", 3, "To Do", "T1:0:DEV", 0, TEST_USERNAME);
        dbManager.createTask("Task 2", "Description 2", "Dev 2", 5, "Done", "T2:1:DEV", 1, TEST_USERNAME);
        dbManager.createTask("Task 3", "Description 3", "Dev 3", 2, "Done", "T3:2:DEV", 2, TEST_USERNAME);
        
        List<DatabaseManager.Task> doneTasks = dbManager.getTasksByStatus(TEST_USERNAME, "Done");
        assertEquals(2, doneTasks.size(), "Should retrieve tasks with 'Done' status");
        
        List<DatabaseManager.Task> todoTasks = dbManager.getTasksByStatus(TEST_USERNAME, "To Do");
        assertEquals(1, todoTasks.size(), "Should retrieve tasks with 'To Do' status");
    }
    
    @Test
    public void testSearchTasksByName() {
        // Register user and create tasks
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        dbManager.createTask("Create Login", "Login feature", "Dev 1", 3, "To Do", "CL:0:DEV", 0, TEST_USERNAME);
        dbManager.createTask("Create Reports", "Report feature", "Dev 2", 5, "Done", "CR:1:DEV", 1, TEST_USERNAME);
        
        List<DatabaseManager.Task> loginTasks = dbManager.searchTasksByName("Create Login", TEST_USERNAME);
        assertEquals(1, loginTasks.size(), "Should find task by exact name");
        assertEquals("Create Login", loginTasks.get(0).getTaskName(), "Task name should match");
    }
    
    @Test
    public void testSearchTasksByDeveloper() {
        // Register user and create tasks
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        dbManager.createTask("Task 1", "Description 1", "Mike Smith", 3, "To Do", "T1:0:SMI", 0, TEST_USERNAME);
        dbManager.createTask("Task 2", "Description 2", "Mike Smith", 5, "Done", "T2:1:SMI", 1, TEST_USERNAME);
        dbManager.createTask("Task 3", "Description 3", "Jane Doe", 2, "Doing", "T3:2:DOE", 2, TEST_USERNAME);
        
        List<DatabaseManager.Task> mikeTasks = dbManager.searchTasksByDeveloper("Mike Smith", TEST_USERNAME);
        assertEquals(2, mikeTasks.size(), "Should find all tasks by Mike Smith");
        
        List<DatabaseManager.Task> janeTasks = dbManager.searchTasksByDeveloper("Jane Doe", TEST_USERNAME);
        assertEquals(1, janeTasks.size(), "Should find task by Jane Doe");
    }
    
    @Test
    public void testUpdateTaskStatus() {
        // Register user and create task
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        dbManager.createTask("Test Task", "Description", "Developer", 3, "To Do", "TT:0:DEV", 0, TEST_USERNAME);
        
        boolean result = dbManager.updateTaskStatus(TEST_USERNAME, "TT:0:DEV", "Done");
        assertTrue(result, "Task status update should succeed");
        
        List<DatabaseManager.Task> doneTasks = dbManager.getTasksByStatus(TEST_USERNAME, "Done");
        assertEquals(1, doneTasks.size(), "Task should be in 'Done' status");
    }
    
    @Test
    public void testDeleteTask() {
        // Register user and create task
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        dbManager.createTask("Test Task", "Description", "Developer", 3, "To Do", "TT:0:DEV", 0, TEST_USERNAME);
        
        boolean result = dbManager.deleteTask(TEST_USERNAME, "TT:0:DEV");
        assertTrue(result, "Task deletion should succeed");
        
        List<DatabaseManager.Task> allTasks = dbManager.getAllTasks(TEST_USERNAME);
        assertEquals(0, allTasks.size(), "No tasks should remain after deletion");
    }
    
    @Test
    public void testGetTaskWithHighestDuration() {
        // Register user and create tasks with different durations
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        dbManager.createTask("Task 1", "Description 1", "Dev 1", 3, "To Do", "T1:0:DEV", 0, TEST_USERNAME);
        dbManager.createTask("Task 2", "Description 2", "Dev 2", 8, "Done", "T2:1:DEV", 1, TEST_USERNAME);
        dbManager.createTask("Task 3", "Description 3", "Dev 3", 5, "Doing", "T3:2:DEV", 2, TEST_USERNAME);
        
        DatabaseManager.Task highestTask = dbManager.getTaskWithHighestDuration(TEST_USERNAME);
        assertNotNull(highestTask, "Should find task with highest duration");
        assertEquals(8, highestTask.getTaskDuration(), "Highest duration should be 8");
        assertEquals("Task 2", highestTask.getTaskName(), "Should be Task 2");
    }
    
    @Test
    public void testGetTotalTaskHours() {
        // Register user and create tasks
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        dbManager.createTask("Task 1", "Description 1", "Dev 1", 3, "To Do", "T1:0:DEV", 0, TEST_USERNAME);
        dbManager.createTask("Task 2", "Description 2", "Dev 2", 5, "Done", "T2:1:DEV", 1, TEST_USERNAME);
        dbManager.createTask("Task 3", "Description 3", "Dev 3", 2, "Doing", "T3:2:DEV", 2, TEST_USERNAME);
        
        int totalHours = dbManager.getTotalTaskHours(TEST_USERNAME);
        assertEquals(10, totalHours, "Total task hours should be 10 (3+5+2)");
    }
    
    @Test
    public void testUserIsolation() {
        // Register two users
        String user1 = "user1";
        String user2 = "user2";
        dbManager.registerUser(user1, TEST_PASSWORD, "User", "One");
        dbManager.registerUser(user2, TEST_PASSWORD, "User", "Two");
        
        // Create tasks for each user
        dbManager.createTask("User1 Task", "Description", "Dev1", 3, "To Do", "U1:0:DEV", 0, user1);
        dbManager.createTask("User2 Task", "Description", "Dev2", 5, "Done", "U2:0:DEV", 0, user2);
        
        // Verify user isolation
        List<DatabaseManager.Task> user1Tasks = dbManager.getAllTasks(user1);
        List<DatabaseManager.Task> user2Tasks = dbManager.getAllTasks(user2);
        
        assertEquals(1, user1Tasks.size(), "User1 should only see their tasks");
        assertEquals(1, user2Tasks.size(), "User2 should only see their tasks");
        assertEquals("User1 Task", user1Tasks.get(0).getTaskName(), "User1 should see their task");
        assertEquals("User2 Task", user2Tasks.get(0).getTaskName(), "User2 should see their task");
        
        // Cleanup
        dbManager.deleteUser(user1);
        dbManager.deleteUser(user2);
    }
}