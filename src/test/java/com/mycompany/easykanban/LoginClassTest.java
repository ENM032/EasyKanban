/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.easykanban;
//Importing the necessary packages to run the unit tests
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
/**
 *
 * @author ebenm
 */
public class LoginClassTest {
    
  Login getLoginMethodsObj = new Login();

    //a test to determine if the username is correctly formatted. 
    @Test
    public void usernameEqualsCorrectFormat() {
        // First register a user to test login
        getLoginMethodsObj.registerUser("kyle_test2", "Test@123", "Kyle", "Buckets");
        String expectedMessage = "Welcome Kyle Buckets, it is great to see you again.";
        assertEquals(expectedMessage, getLoginMethodsObj.loginUser("kyle_test2", "Test@123"));
    }
    
    //a test to determine if the username is not correctly formatted.
    @Test
    public void usernameEqualsIncorrectFormat() {
        String returnMessage = "Username is not correctly formatted, please ensure\nthat your username is 3-20 characters long and\ncontains only letters, numbers, and underscores.";
        assertEquals(returnMessage, getLoginMethodsObj.registerUser("ab", "W0rld0fW@rcraft", "Kyle", "Test"));
    }
    
    //a test to determine if the password meets the complexity requirements.
    @Test
    public void passwordRequirementsEqualsTrue() {
        String returnMessage = "Welcome to EasyKanban! Your account has been created successfully.\nYou can now log in with your credentials.";
        String uniqueUsername = "user" + (System.currentTimeMillis() % 10000);
        assertEquals(returnMessage, getLoginMethodsObj.registerUser(uniqueUsername, "Ch&&sec@ke99!", "Pro", "Gamer"));
    }
    
    //a test to determine if the password is not correctly formatted.
    @Test
    public void passwordRequirementsEqualsFalse() {
        getLoginMethodsObj.registerUser("Pr_Og", "password", "Pro", "Test");
        //text blocks caused multiple errors during github workflow, hence i removed them from the project
        String returnMessage = "Please create a stronger password.\n" +
                               "Password must contain at least 8 characters,\n" +
                               "including one uppercase letter, one number, and one special character.";
        assertEquals(returnMessage, getLoginMethodsObj.registerUser("ky_1", "password", "Kyle", "Test"));
        
    }
    
    // a test that returns a true value if the login proccess was succesful.
    @Test
    public void loginSuccessIsTrue() {
        // Register a user first
        getLoginMethodsObj.registerUser("test_login", "Test@123", "Test", "User");
        // Test login with database method
        DatabaseManager dbManager = DatabaseManager.getInstance();
        DatabaseManager.User user = dbManager.authenticateUser("test_login", "Test@123");
        assertTrue(user != null); 
    }
    
    // a test that returns a false value if the login proccess was unsuccesful.
    @Test
    public void loginSuccessIsFalse() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        DatabaseManager.User user = dbManager.authenticateUser("nonexistent", "wrongpass");
        assertFalse(user != null);
    }
    
    //a test that returns a true value if the username is correctly formatted.
    @Test
    public void usernameIsCorrectlyFormatted() {
        assertTrue(getLoginMethodsObj.checkUserName("valid_user123"));
    }
    
    //a test that returns a false value if the username is not correctly formatted.
    @Test
    public void usernameIsIncorrectlyFormatted() {
        assertFalse(getLoginMethodsObj.checkUserName("ab")); // Too short
    }
    
    //a test that returns a true value if the password meets the password complexity requirements.
    @Test
    public void passwordMeetsComplexityRequirements() {
        assertTrue(getLoginMethodsObj.checkPasswordComplexity("Math!3asy"));
    }
    
    //a test that returns a false value if the password does not meet the password complexity requirements.
    @Test
    public void passwordDoesNotMeetsComplexityRequirements() {
        assertFalse(getLoginMethodsObj.checkPasswordComplexity("TottenhamHotspur"));  
    }
    
    // New Database Integration Tests
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "Test@123";
    private static final String TEST_FIRSTNAME = "John";
    private static final String TEST_LASTNAME = "Doe";
    private DatabaseManager dbManager;
    
    @BeforeEach
    public void setUpDatabase() {
        dbManager = DatabaseManager.getInstance();
        // Clean up any existing test data
        try {
            dbManager.deleteUser(TEST_USERNAME);
        } catch (Exception e) {
            // Ignore if user doesn't exist
        }
    }
    
    @AfterEach
    public void tearDownDatabase() {
        // Clean up test data
        try {
            dbManager.deleteUser(TEST_USERNAME);
        } catch (Exception e) {
            // Ignore cleanup errors
        }
        Login.setCurrentUser(null);
    }
    
    @Test
    public void testDatabaseUserRegistration() {
        String result = getLoginMethodsObj.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        assertEquals("Registration successful! You can now log in.", result, "Database user registration should succeed");
        
        // Test duplicate registration
        String duplicateResult = getLoginMethodsObj.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        assertTrue(duplicateResult.contains("Username already exists"), "Duplicate registration should fail");
    }
    
    @Test
    public void testDatabaseUserRegistrationWithInvalidUsername() {
        String result = getLoginMethodsObj.registerUser("verylongusernamethatexceedslimit", TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        assertTrue(result.contains("Username is not correctly formatted"), 
                  "Should reject invalid username format");
    }
    
    @Test
    public void testDatabaseUserRegistrationWithInvalidPassword() {
        String result = getLoginMethodsObj.registerUser(TEST_USERNAME, "weak", TEST_FIRSTNAME, TEST_LASTNAME);
        assertTrue(result.contains("Password is not correctly formatted"), 
                  "Should reject weak password");
    }
    
    @Test
    public void testDatabaseAuthentication() {
        // Register user first
        getLoginMethodsObj.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        
        // Test successful authentication
        boolean authResult = getLoginMethodsObj.authenticateUser(TEST_USERNAME, TEST_PASSWORD);
        assertTrue(authResult, "Authentication should succeed with correct credentials");
        
        // Verify current user is set
        assertTrue(Login.isUserLoggedIn(), "User should be logged in after successful authentication");
        DatabaseManager.User currentUser = Login.getCurrentUser();
        assertNotNull(currentUser, "Current user should not be null");
        assertEquals(TEST_USERNAME, currentUser.getUsername(), "Current user username should match");
        
        // Test failed authentication
        Login.logout(); // Clear current user
        boolean failedAuth = getLoginMethodsObj.authenticateUser(TEST_USERNAME, "wrongPassword");
        assertFalse(failedAuth, "Authentication should fail with incorrect password");
        assertFalse(Login.isUserLoggedIn(), "User should not be logged in after failed authentication");
    }
    
    @Test
    public void testUserSessionManagement() {
        // Register and authenticate user
        getLoginMethodsObj.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        getLoginMethodsObj.authenticateUser(TEST_USERNAME, TEST_PASSWORD);
        
        // Test user is logged in
        assertTrue(Login.isUserLoggedIn(), "User should be logged in");
        
        // Test logout
        Login.logout();
        assertFalse(Login.isUserLoggedIn(), "User should be logged out");
        assertNull(Login.getCurrentUser(), "Current user should be null after logout");
    }
    
    @Test
    public void testGetCurrentUserDetails() {
        // Register and authenticate user
        getLoginMethodsObj.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        getLoginMethodsObj.authenticateUser(TEST_USERNAME, TEST_PASSWORD);
        
        DatabaseManager.User currentUser = Login.getCurrentUser();
        assertNotNull(currentUser, "Current user should not be null");
        assertEquals(TEST_USERNAME, currentUser.getUsername(), "Username should match");
        assertEquals(TEST_FIRSTNAME, currentUser.getFirstName(), "First name should match");
        assertEquals(TEST_LASTNAME, currentUser.getLastName(), "Last name should match");
    }
    
    @Test
    public void testSetCurrentUser() {
        // Create a user object
        DatabaseManager.User testUser = new DatabaseManager.User(1, TEST_USERNAME, TEST_FIRSTNAME, TEST_LASTNAME);
        
        // Set current user
        Login.setCurrentUser(testUser);
        
        // Verify current user is set
        assertTrue(Login.isUserLoggedIn(), "User should be logged in");
        assertEquals(testUser, Login.getCurrentUser(), "Current user should match set user");
        
        // Test setting null user
        Login.setCurrentUser(null);
        assertFalse(Login.isUserLoggedIn(), "User should not be logged in when set to null");
        assertNull(Login.getCurrentUser(), "Current user should be null");
    }
    
    @Test
    public void testPasswordHashing() {
        // Register user and verify password is hashed
        getLoginMethodsObj.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        
        // Authenticate to verify password hashing works
        boolean authResult = getLoginMethodsObj.authenticateUser(TEST_USERNAME, TEST_PASSWORD);
        assertTrue(authResult, "Authentication should work with hashed password");
        
        // Verify that wrong password fails
        Login.logout();
        boolean wrongPasswordAuth = getLoginMethodsObj.authenticateUser(TEST_USERNAME, "wrongPassword");
        assertFalse(wrongPasswordAuth, "Authentication should fail with wrong password");
    }
    
    @Test
    public void testBackwardCompatibilityMethods() {
        // Test deprecated methods still work but redirect to new functionality
        
        // Register user first
        getLoginMethodsObj.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        
        // Test deprecated loginStatus method
        String loginResult = Login.loginStatus(TEST_USERNAME, TEST_PASSWORD);
        assertTrue(loginResult.contains("Welcome") || loginResult.contains("successful"), 
                  "Deprecated loginStatus should still work");
        
        // Test deprecated findCredentialsInTextFile method
        boolean findResult = Login.findCredentialsInTextFile(TEST_USERNAME, TEST_PASSWORD);
        assertTrue(findResult, "Deprecated findCredentialsInTextFile should still work");
    }
}
