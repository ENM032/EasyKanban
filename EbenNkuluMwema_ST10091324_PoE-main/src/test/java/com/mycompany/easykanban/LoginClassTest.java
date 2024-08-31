/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.easykanban;
//Importing the necessary packages to run the unit tests
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
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
        assertEquals(returnMessage, getLoginMethodsObj.registerUser("kyle!!!!!!!!", "W0rld0fW@rcraft"));
    }
    
    //a test to determine if the password is correctly formatted.
    @Test
    public void passwordRequirementsEqualsTrue() {
        String returnMessage = "Password successfully captured";
        assertEquals(returnMessage, getLoginMethodsObj.registerUser("Pr_OG", "Ch&&sec@ke99!"));
    }
    
    //a test to determine if the password is not correctly formatted.
    @Test
    public void passwordRequirementsEqualsFalse() {
        getLoginMethodsObj.registerUser("Pr_Og", "password");
        //text blocks caused multiple errors during github workflow, hence i removed them from the project
        String returnMessage = "Password is not correctly formatted, please ensure\nthat your password contains " +
                               "at least 8 characters,\na capital letter, a number and a special character.";
        assertEquals(returnMessage, getLoginMethodsObj.registerUser("ky_1", "password"));
        
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
}
