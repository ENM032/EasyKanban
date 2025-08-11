package com.mycompany.easykanban;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Integration tests for Tasks class database functionality
 * Tests the new database-integrated methods in Tasks class
 */
public class TasksDatabaseIntegrationTest {
    
    private Tasks tasks;
    private DatabaseManager dbManager;
    private static final String TEST_USERNAME = "taskTestUser";
    private static final String TEST_PASSWORD = "Test@123";
    private static final String TEST_FIRSTNAME = "Task";
    private static final String TEST_LASTNAME = "Tester";
    
    @BeforeEach
    public void setUp() {
        tasks = new Tasks();
        dbManager = DatabaseManager.getInstance();
        
        // Clean up any existing test data
        try {
            dbManager.deleteUser(TEST_USERNAME);
        } catch (Exception e) {
            // Ignore if user doesn't exist
        }
        
        // Register test user
        dbManager.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        
        // Set current user in Login class for testing
        Login.setCurrentUser(dbManager.authenticateUser(TEST_USERNAME, TEST_PASSWORD));
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up test data
        try {
            dbManager.deleteUser(TEST_USERNAME);
        } catch (Exception e) {
            // Ignore cleanup errors
        }
        Login.setCurrentUser(null);
    }
    
    @Test
    public void testCreateTaskInDatabase() {
        boolean result = tasks.createTaskInDatabase(
            "Test Task", "Test Description", "John Developer", 5, "To Do", "TST:0:DEV"
        );
        assertTrue(result, "Task creation in database should succeed");
        
        // Verify task was created
        List<DatabaseManager.Task> allTasks = dbManager.getAllTasks(TEST_USERNAME);
        assertEquals(1, allTasks.size(), "Should have one task in database");
        assertEquals("Test Task", allTasks.get(0).getTaskName(), "Task name should match");
    }
    
    @Test
    public void testGetTotalTaskHoursFromDatabase() {
        // Create test tasks
        tasks.createTaskInDatabase("Task 1", "Description 1", "Dev 1", 3, "To Do", "T1:0:DEV");
        tasks.createTaskInDatabase("Task 2", "Description 2", "Dev 2", 7, "Done", "T2:1:DEV");
        tasks.createTaskInDatabase("Task 3", "Description 3", "Dev 3", 2, "Doing", "T3:2:DEV");
        
        int totalHours = tasks.getTotalTaskHoursFromDatabase(TEST_USERNAME);
        assertEquals(12, totalHours, "Total hours should be 12 (3+7+2)");
    }
    
    @Test
    public void testFindHighestDurationFromDatabase() {
        // Create test tasks with different durations
        tasks.createTaskInDatabase("Short Task", "Description 1", "Dev 1", 2, "To Do", "ST:0:DEV");
        tasks.createTaskInDatabase("Long Task", "Description 2", "Dev 2", 10, "Done", "LT:1:DEV");
        tasks.createTaskInDatabase("Medium Task", "Description 3", "Dev 3", 5, "Doing", "MT:2:DEV");
        
        String result = tasks.findHighestDurationFromDatabase(TEST_USERNAME);
        assertTrue(result.contains("Long Task"), "Should find the longest duration task");
        assertTrue(result.contains("Dev 2"), "Should include developer name");
        assertTrue(result.contains("10"), "Should include duration");
    }
    
    @Test
    public void testDisplayCompletedTasksFromDatabase() {
        // Create test tasks with different statuses
        tasks.createTaskInDatabase("Task 1", "Description 1", "Dev 1", 3, "To Do", "T1:0:DEV");
        tasks.createTaskInDatabase("Completed Task 1", "Description 2", "Dev 2", 5, "Done", "CT1:1:DEV");
        tasks.createTaskInDatabase("Task 3", "Description 3", "Dev 3", 2, "Doing", "T3:2:DEV");
        tasks.createTaskInDatabase("Completed Task 2", "Description 4", "Dev 4", 4, "Done", "CT2:3:DEV");
        
        tasks.displayCompletedTasksFromDatabase(TEST_USERNAME);
        // Note: This method returns void, so we can't test the return value directly
        // The method displays results via JOptionPane, which is tested through integration
    }
    
    @Test
    public void testFindTaskByNameFromDatabase() {
        // Create test tasks
        tasks.createTaskInDatabase("Create Login", "Login feature", "Mike Smith", 5, "To Do", "CL:0:SMI");
        tasks.createTaskInDatabase("Create Reports", "Report feature", "Jane Doe", 3, "Done", "CR:1:DOE");
        
        String result = tasks.findTaskByNameFromDatabase("Create Login", TEST_USERNAME);
        assertTrue(result.contains("Mike Smith"), "Should include developer name");
        assertTrue(result.contains("Create Login"), "Should include task name");
        
        String notFoundResult = tasks.findTaskByNameFromDatabase("Non-existent Task", TEST_USERNAME);
        assertTrue(notFoundResult.contains("not found") || notFoundResult.contains("No task"), 
                  "Should indicate task not found");
    }
    
    @Test
    public void testFindTasksByDeveloperFromDatabase() {
        // Create test tasks
        tasks.createTaskInDatabase("Task 1", "Description 1", "Mike Smith", 3, "To Do", "T1:0:SMI");
        tasks.createTaskInDatabase("Task 2", "Description 2", "Mike Smith", 5, "Done", "T2:1:SMI");
        tasks.createTaskInDatabase("Task 3", "Description 3", "Jane Doe", 2, "Doing", "T3:2:DOE");
        
        String mikeResult = tasks.findTasksByDeveloperFromDatabase("Mike Smith", TEST_USERNAME);
        assertTrue(mikeResult.contains("Task 1"), "Should include Mike's first task");
        assertTrue(mikeResult.contains("Task 2"), "Should include Mike's second task");
        assertFalse(mikeResult.contains("Task 3"), "Should not include Jane's task");
        
        String janeResult = tasks.findTasksByDeveloperFromDatabase("Jane Doe", TEST_USERNAME);
        assertTrue(janeResult.contains("Task 3"), "Should include Jane's task");
        assertFalse(janeResult.contains("Task 1"), "Should not include Mike's tasks");
        
        String notFoundResult = tasks.findTasksByDeveloperFromDatabase("Non-existent Developer", TEST_USERNAME);
        assertTrue(notFoundResult.contains("not found") || notFoundResult.contains("No tasks"), 
                  "Should indicate no tasks found");
    }
    
    @Test
    public void testDeleteTaskFromDatabase() {
        // Create test task
        tasks.createTaskInDatabase("Task to Delete", "Description", "Developer", 3, "To Do", "TTD:0:DEV");
        
        // Verify task exists
        List<DatabaseManager.Task> beforeDelete = dbManager.getAllTasks(TEST_USERNAME);
        assertEquals(1, beforeDelete.size(), "Should have one task before deletion");
        
        String result = tasks.deleteTaskFromDatabase("Task to Delete", TEST_USERNAME);
        assertTrue(result.contains("successfully deleted") || result.contains("deleted successfully"), 
                  "Should confirm successful deletion");
        
        // Verify task was deleted
        List<DatabaseManager.Task> afterDelete = dbManager.getAllTasks(TEST_USERNAME);
        assertEquals(0, afterDelete.size(), "Should have no tasks after deletion");
        
        // Test deleting non-existent task
        String notFoundResult = tasks.deleteTaskFromDatabase("Non-existent Task", TEST_USERNAME);
        assertTrue(notFoundResult.contains("not found") || notFoundResult.contains("does not exist"), 
                  "Should indicate task not found");
    }
    
    @Test
    public void testDisplayAllTasksFromDatabase() {
        // Create test tasks
        tasks.createTaskInDatabase("Task 1", "Description 1", "Dev 1", 3, "To Do", "T1:0:DEV");
        tasks.createTaskInDatabase("Task 2", "Description 2", "Dev 2", 5, "Done", "T2:1:DEV");
        tasks.createTaskInDatabase("Task 3", "Description 3", "Dev 3", 2, "Doing", "T3:2:DEV");
        
        String result = tasks.displayAllTasksFromDatabase(TEST_USERNAME);
        assertTrue(result.contains("Task 1"), "Should include first task");
        assertTrue(result.contains("Task 2"), "Should include second task");
        assertTrue(result.contains("Task 3"), "Should include third task");
        assertTrue(result.contains("Dev 1"), "Should include first developer");
        assertTrue(result.contains("Dev 2"), "Should include second developer");
        assertTrue(result.contains("Dev 3"), "Should include third developer");
    }
    
    @Test
    public void testDatabaseMethodsWithNoUser() {
        // Test behavior when no user is logged in
        Login.setCurrentUser(null);
        
        boolean createResult = tasks.createTaskInDatabase(
            "Test Task", "Description", "Developer", 5, "To Do", "TT:0:DEV"
        );
        assertFalse(createResult, "Task creation should fail when no user is logged in");
        
        int totalHours = tasks.getTotalTaskHoursFromDatabase(null);
        assertEquals(0, totalHours, "Total hours should be 0 when no user is logged in");
        
        String highestDuration = tasks.findHighestDurationFromDatabase(null);
        assertTrue(highestDuration.contains("No user") || highestDuration.contains("not logged in"), 
                  "Should indicate no user logged in");
    }
    
    @Test
    public void testDatabaseMethodsWithEmptyDatabase() {
        // Test behavior with empty database (no tasks)
        int totalHours = tasks.getTotalTaskHoursFromDatabase(TEST_USERNAME);
        assertEquals(0, totalHours, "Total hours should be 0 with no tasks");
        
        String highestDuration = tasks.findHighestDurationFromDatabase(TEST_USERNAME);
        assertTrue(highestDuration.contains("No tasks") || highestDuration.contains("not found"), 
                  "Should indicate no tasks found");
        
        tasks.displayCompletedTasksFromDatabase(TEST_USERNAME);
        // Note: This method returns void, testing through integration
        
        String allTasks = tasks.displayAllTasksFromDatabase(TEST_USERNAME);
        assertTrue(allTasks.contains("No tasks") || allTasks.contains("not found"), 
                  "Should indicate no tasks found");
    }
}