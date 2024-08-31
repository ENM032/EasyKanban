/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.easykanban;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author ebenm
 */
public class Login {
    //Instantiating these variables here so they can be used inside various methods
    private static String name;
    private static String surname;
    
    //validating whether or not the username recieved from the JTextField meets all the requirements
    //if it does, a true value is returned and if it doesn't, a false value is returned 
    Boolean checkUserName(String userName){
      return userName.contains("_") && userName.length() <= 5;
    }
    
    //validating whether or not the password received from the JPasswordField meets the minimum password complexity requirements
    //if it does, a true value is returned and if it doesn't, a false value is returned
    Boolean checkPasswordComplexity(String password){
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
    
    //looping through the text file to test whether or not a username or password already exists
    //if it does, the user will be asked to enter different credentials and if it doesn't,
    //the user will be allowed register their new account
    public static String checkIfCredentialsExistsInTextFile(String username, String password){
     boolean credentialsFound = false;
     String ifFoundMessage = "";
     String locatedUserName;
     String locatedPassword;
     try{
         Scanner textFileScanner = new Scanner(new File("Credentials.txt"));
         textFileScanner.useDelimiter("[,\n]");
    //reading records inside the credentials text file until it matches the username and password from the JTextField and JPasswordField
        while(textFileScanner.hasNext() && !credentialsFound){
              locatedUserName = textFileScanner.next();
              locatedPassword = textFileScanner.next();
              name = textFileScanner.next();
              surname = textFileScanner.next();

        if(locatedUserName.trim().equals(username.trim()) && locatedPassword.trim().equals(password.trim())){
            credentialsFound = true;
             ifFoundMessage = "Both credentials found";
        }
        else if (locatedUserName.trim().equals(username.trim())){
            credentialsFound = true;
             ifFoundMessage = "Username found";
         }
        else if(locatedPassword.trim().equals(password.trim())){
            credentialsFound = true;
             ifFoundMessage = "Password found";
        }
    }
     textFileScanner.close();
    }
     catch(FileNotFoundException fnfe){
           System.out.println("Something went wrong! Could not locate text file");
    }
     catch(NoSuchElementException nsee){
         ifFoundMessage = "Nothing found";
     }
     return ifFoundMessage;
    }
    
    /*displaying a suitable message if the credentials are found inside the text file
    if the credentials aren't found, the credntials will be checked for correct formatting and complexity requirements
    if they do meet all the requirements a suitable message is displayed, and they will be able to create a new account
    if they do not, a suitable message is displayed and the user won't be able to create a new account
    */
     String registerUser(String username, String password){
        String resultMessage = "";
        
        if (checkIfCredentialsExistsInTextFile(username, password).equals("Username found")){
             resultMessage = "Username already exist\nplease enter a different username.";
             JOptionPane.showMessageDialog(null, resultMessage, "Something went wrong", JOptionPane.ERROR_MESSAGE);
        }
        else if (checkIfCredentialsExistsInTextFile(username, password).equals("Password found")){
                 resultMessage = "Password already exist\nplease enter a different password.";
                 JOptionPane.showMessageDialog(null, resultMessage, "Something went wrong", JOptionPane.ERROR_MESSAGE);
        }
        else if (checkIfCredentialsExistsInTextFile(username, password).equals("Both credentials found")){
                 resultMessage = "Username and passowrd already exist\nplease enter a different username and password.";
                 JOptionPane.showMessageDialog(null, resultMessage, "Something went wrong", JOptionPane.ERROR_MESSAGE);
        }        
        else if (!checkUserName(username)){
                 resultMessage = "Username is not correctly formatted, please ensure\nthat your username contains an underscore" +
                                 "\nand is no more than 5 characters in length.";
                 JOptionPane.showMessageDialog(null, resultMessage, "Something went wrong", JOptionPane.ERROR_MESSAGE);
        }
        else if (checkPasswordComplexity(password)){
                 resultMessage = "Password successfully captured";
                 JOptionPane.showMessageDialog(null, resultMessage, "Registration successful", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (!checkPasswordComplexity(password)){
                 resultMessage = "Password is not correctly formatted, please ensure\nthat your password contains at least 8 characters," +
                                 "\na capital letter, a number and a special character.";
                 JOptionPane.showMessageDialog(null, resultMessage, "Something went wrong", JOptionPane.ERROR_MESSAGE);
        }
        return resultMessage;
    } 
     
    //writing the entered credentials into a textfile, if the textfile doesn't exist, a new one will be created and written to
    //if the text file does exist, the credentials will be written into the textfile
    public static String writingToTextFile(String username, String password, String firstname, String lastname){
         File myFile = new File("Credentials.txt");
         try{
             FileWriter textFileWriter = new FileWriter(myFile, true);
             BufferedWriter userCredentialsWriter = new BufferedWriter(textFileWriter);
             userCredentialsWriter.write(username + ", "+ password + ", "+ firstname + ", "+ lastname +"\n");
             userCredentialsWriter.close();
             return "File successfully written to";
        }
        catch(IOException ioe){
         return "Something went wrong! Text File could not be created or written to.";
        }
    }
    
    //locating the entered credentials from the SignInForm page, if the credentials are found a true value is returned
    //if the credentials are not found a false value is returned
    public static Boolean findCredentialsInTextFile(String username, String password){
     boolean credentialsAreFound = false;
     String tempUserName;
     String tempPassword;
     String tempName;
     String tempSurname;
     try{
         Scanner textFileScanner = new Scanner(new File("Credentials.txt"));
         textFileScanner.useDelimiter("[,\n]");
    //Reading records inside the credentials text file until it matches the username and password from the JTextField and JPasswordField
             while(textFileScanner.hasNext() && !credentialsAreFound){
                 tempUserName = textFileScanner.next();
                 tempPassword = textFileScanner.next();
                 tempName = textFileScanner.next();
                 tempSurname = textFileScanner.next();

                if (tempUserName.trim().equals(username.trim()) && tempPassword.trim().equals(password.trim())){
                     credentialsAreFound = true;
                     name = tempName;
                     surname = tempSurname;
                }
    }
     textFileScanner.close();
    }
     catch(FileNotFoundException fnfe){
           System.out.println("Something went wrong! Could not located text file");
    }
     return credentialsAreFound;
    }
    
    //checking if the credentials entered from the SignInForm exist, if they do exist, a suitable message will be displayed and the user will be logged in
    //if they don't exist, a suitable message will be displayed and the user will not be logged in
    public static String loginStatus(String username, String password){
     String returnLoginMessage = "";
        if(findCredentialsInTextFile(username, password)){
             returnLoginMessage ="Welcome" + name + "," + surname + " it is great to see you again.";
             JOptionPane.showMessageDialog(null, returnLoginMessage, "Login successful", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(!findCredentialsInTextFile(username, password)){
                 returnLoginMessage = "Username or password incorrect, please try again.";
                 JOptionPane.showMessageDialog(null, returnLoginMessage, "Something went wrong", JOptionPane.ERROR_MESSAGE);
        }
        return returnLoginMessage;
  }
    
    //creating a method to easily set the user's name when writing and running the JUnit test
    public static void setName(String settedName){
        name = settedName;
    }
    
    //creating a method to easily set the user's surname when writing and running the JUnit test
    public static void setSurname(String settedSurname){
        surname = settedSurname;
    }
    
    //creating a method to access the user's name from other classes or inside other methods
    public static String getName(){
        return name;
    }
    
    //creating a method to access the user's surname from other classes or inside other methods
    public static String getSurname(){
        return surname;
    }
}
