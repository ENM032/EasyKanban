/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.easykanban;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 *
 * @author ebenm
 */
public class Login {
    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
    private static DatabaseManager.User currentUser;
    private final DatabaseManager dbManager;
    
    public Login() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    /**
     * Validates username format requirements
     * @param userName the username to validate
     * @return true if username meets requirements, false otherwise
     */
    public boolean checkUserName(String userName){
        return userName.contains("_") && userName.length() <= 5;
    }
    
    /**
     * Validates password complexity requirements
     * @param password the password to validate
     * @return true if password meets complexity requirements, false otherwise
     */
    public boolean checkPasswordComplexity(String password){
     int isUpperCaseCount = 0;
     int isDigitCount = 0;
     int SpecialCount = 0;
        for(int i= 0; i<password.length(); i++){
             char character = password.charAt(i);
            if (Character.isUpperCase(character))
                 isUpperCaseCount++;
            else if (Character.isDigit(character))
                     isDigitCount++;
            else if (password.contains("!") || password.contains("@") || password.contains("#") || password.contains("$") ||
                     password.contains("%")||  password.contains("&") || password.contains("*") || password.contains("?") ||
                     password.contains("-") || password.contains("<")|| password.contains(">") || password.contains(".") ||
                     password.contains(",") || password.contains("_") || password.contains(":") || password.contains("/") ||
                     password.contains("^") || password.contains("|") || password.contains("+") || password.contains("(") ||
                     password.contains(")"))
                     SpecialCount++;
      }
        return isUpperCaseCount >= 1 && isDigitCount >=1 && SpecialCount >=1 && password.length() >= 8;
    }
    
    /**
     * Check if username already exists in database
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean checkIfUsernameExists(String username) {
        return dbManager.usernameExists(username);
    }
    
    /**
     * Register a new user with validation
     * @param username the username
     * @param password the password
     * @param firstName the first name
     * @param lastName the last name
     * @return result message indicating success or failure
     */
    public String registerUser(String username, String password, String firstName, String lastName) {
        String resultMessage = "";
        
        // Validate username format
        if (!checkUserName(username)) {
            resultMessage = "Username is not correctly formatted, please ensure\n" +
                           "that your username contains an underscore\n" +
                           "and is no more than 5 characters in length.";
            JOptionPane.showMessageDialog(null, resultMessage, "Invalid Username", JOptionPane.ERROR_MESSAGE);
            return resultMessage;
        }
        
        // Validate password complexity
        if (!checkPasswordComplexity(password)) {
            resultMessage = "Password is not correctly formatted, please ensure\n" +
                           "that your password contains at least 8 characters,\n" +
                           "a capital letter, a number and a special character.";
            JOptionPane.showMessageDialog(null, resultMessage, "Invalid Password", JOptionPane.ERROR_MESSAGE);
            return resultMessage;
        }
        
        // Check if username already exists
        if (checkIfUsernameExists(username)) {
            resultMessage = "Username already exists\nplease enter a different username.";
            JOptionPane.showMessageDialog(null, resultMessage, "Username Taken", JOptionPane.ERROR_MESSAGE);
            return resultMessage;
        }
        
        // Validate names are not empty
        if (firstName == null || firstName.trim().isEmpty() || 
            lastName == null || lastName.trim().isEmpty()) {
            resultMessage = "First name and last name are required.";
            JOptionPane.showMessageDialog(null, resultMessage, "Missing Information", JOptionPane.ERROR_MESSAGE);
            return resultMessage;
        }
        
        // Attempt to register user
        if (dbManager.registerUser(username, password, firstName.trim(), lastName.trim())) {
            resultMessage = "Registration successful! You can now log in.";
            JOptionPane.showMessageDialog(null, resultMessage, "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
            LOGGER.info("User registered successfully: " + username);
        } else {
            resultMessage = "Registration failed. Please try again.";
            JOptionPane.showMessageDialog(null, resultMessage, "Registration Failed", JOptionPane.ERROR_MESSAGE);
            LOGGER.warning("Registration failed for username: " + username);
        }
        
        return resultMessage;
    } 
     
    /**
     * Legacy method for backward compatibility - now uses database
     * @deprecated Use registerUser instead
     */
    @Deprecated
    public static String writingToTextFile(String username, String password, String firstname, String lastname){
        DatabaseManager dbManager = DatabaseManager.getInstance();
        if (dbManager.registerUser(username, password, firstname, lastname)) {
            return "User successfully registered in database";
        } else {
            return "Registration failed";
        }
    }
    
    /**
     * Legacy method for backward compatibility - now uses database
     * @deprecated Use loginUser instead
     */
    @Deprecated
    public static Boolean findCredentialsInTextFile(String username, String password){
        DatabaseManager dbManager = DatabaseManager.getInstance();
        DatabaseManager.User user = dbManager.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    /**
     * Authenticate user login with database
     * @param username the username
     * @param password the password
     * @return login status message
     */
    public String loginUser(String username, String password) {
        String returnLoginMessage = "";
        
        // Validate input
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            returnLoginMessage = "Username and password are required.";
            JOptionPane.showMessageDialog(null, returnLoginMessage, "Missing Credentials", JOptionPane.ERROR_MESSAGE);
            return returnLoginMessage;
        }
        
        // Attempt authentication
        DatabaseManager.User user = dbManager.authenticateUser(username.trim(), password);
        
        if (user != null) {
            currentUser = user;
            returnLoginMessage = "Welcome " + user.getFirstName() + " " + user.getLastName() + 
                               ", it is great to see you again.";
            JOptionPane.showMessageDialog(null, returnLoginMessage, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            LOGGER.info("User logged in successfully: " + username);
        } else {
            returnLoginMessage = "Username or password incorrect, please try again.";
            JOptionPane.showMessageDialog(null, returnLoginMessage, "Login Failed", JOptionPane.ERROR_MESSAGE);
            LOGGER.warning("Login failed for username: " + username);
        }
        
        return returnLoginMessage;
    }
    
    /**
     * Legacy method for backward compatibility
     * @deprecated Use loginUser instead
     */
    @Deprecated
    public static String loginStatus(String username, String password){
        Login login = new Login();
        return login.loginUser(username, password);
    }
    
    /**
     * Get the currently logged-in user
     * @return the current user or null if no user is logged in
     */
    public static DatabaseManager.User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Set the current user (for testing purposes)
     * @param user the user to set as current
     */
    public static void setCurrentUser(DatabaseManager.User user) {
        currentUser = user;
    }
    
    /**
     * Check if a user is currently logged in
     * @return true if a user is logged in, false otherwise
     */
    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Log out the current user
     */
    public static void logout() {
        currentUser = null;
        LOGGER.info("User logged out");
    }
    
    /**
     * Authenticate user and return boolean result (for testing)
     * @param username the username
     * @param password the password
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        DatabaseManager.User user = dbManager.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
            LOGGER.info("User authenticated successfully: " + username);
            return true;
        }
        LOGGER.warning("Authentication failed for username: " + username);
        return false;
    }
    
    /**
     * Legacy method for backward compatibility
     * @deprecated Use getCurrentUser().getFirstName() instead
     */
    @Deprecated
    public static String getName(){
        return currentUser != null ? currentUser.getFirstName() : null;
    }
    
    /**
     * Legacy method for backward compatibility
     * @deprecated Use getCurrentUser().getLastName() instead
     */
    @Deprecated
    public static String getSurname(){
        return currentUser != null ? currentUser.getLastName() : null;
    }
    
    /**
     * Legacy method for testing
     * @deprecated Use setCurrentUser instead
     */
    @Deprecated
    public static void setName(String settedName){
        // This method is deprecated and no longer functional
        LOGGER.warning("setName method is deprecated and no longer functional");
    }
    
    /**
     * Legacy method for testing
     * @deprecated Use setCurrentUser instead
     */
    @Deprecated
    public static void setSurname(String settedSurname){
        // This method is deprecated and no longer functional
        LOGGER.warning("setSurname method is deprecated and no longer functional");
    }
}
