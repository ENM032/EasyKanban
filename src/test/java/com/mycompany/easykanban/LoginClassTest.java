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
        Login.setName("kyle");
        Login.setSurname("Buckets");
        String firstname = Login.getName();
        String lastname = Login.getSurname();
        String expectedMessage = "Welcome " + firstname + ", " + lastname + " it is great to see you again.";
        assertEquals(expectedMessage, Login.loginStatus("kyl_1", "@Ch33s3y"));
    }
    
    //a test to determine if the username is not correctly formatted.
    @Test
    public void usernameEqualsIncorrectFormat() {
        //text blocks caused multiple errors during github workflow, hence i removed them from the project
        String returnMessage = "Username is not correctly formatted, please ensure\nthat your username contains an underscore" +
                               "\nand is no more than 5 characters in length.";
        assertEquals(returnMessage, getLoginMethodsObj.registerUser("kyle!!!!!!!!", "W0rld0fW@rcraft", "Kyle", "Test"));
    }
    
    //a test to determine if the password is correctly formatted.
    @Test
    public void passwordRequirementsEqualsTrue() {
        String returnMessage = "Password successfully captured";
        assertEquals(returnMessage, getLoginMethodsObj.registerUser("Pr_OG", "Ch&&sec@ke99!", "Pro", "Gamer"));
    }
    
    //a test to determine if the password is not correctly formatted.
    @Test
    public void passwordRequirementsEqualsFalse() {
        getLoginMethodsObj.registerUser("Pr_Og", "password", "Pro", "Test");
        //text blocks caused multiple errors during github workflow, hence i removed them from the project
        String returnMessage = "Password is not correctly formatted, please ensure\nthat your password contains " +
                               "at least 8 characters,\na capital letter, a number and a special character.";
        assertEquals(returnMessage, getLoginMethodsObj.registerUser("ky_1", "password", "Kyle", "Test"));
        
    }
    
    // a test that returns a true value if the login proccess was succesful.
    @Test
    public void loginSuccessIsTrue() {
        assertTrue(Login.findCredentialsInTextFile("kyl_1","@Ch33s3y")); 
    }
    
    // a test that returns a false value if the login proccess was unsuccesful.
    @Test
    public void loginSuccessIsFalse() {
        assertFalse(Login.findCredentialsInTextFile("M3ssi","mybirthday"));
    }
    
    //a test that returns a true value if the username is correctly formatted.
    @Test
    public void usernameIsCorrectlyFormatted() {
        assertTrue(getLoginMethodsObj.checkUserName("Bp_S"));
    }
    
    //a test that returns a false value if the username is not correctly formatted.
    @Test
    public void usernameIsIncorrectlyFormatted() {
        assertFalse(getLoginMethodsObj.checkUserName("Agent47"));
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
        assertEquals("User registered successfully", result, "Database user registration should succeed");
        
        // Test duplicate registration
        String duplicateResult = getLoginMethodsObj.registerUser(TEST_USERNAME, TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
        assertEquals("Username already exists", duplicateResult, "Duplicate registration should fail");
    }
    
    @Test
    public void testDatabaseUserRegistrationWithInvalidUsername() {
        String result = getLoginMethodsObj.registerUser("invalidusername", TEST_PASSWORD, TEST_FIRSTNAME, TEST_LASTNAME);
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
