/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.easykanban;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ebenm
 */
public class TaskClassTest {
    //Created for task 3 of PoE
    //array various obtained from PROG5121 PoE pdf guideline
    private static final String[] taskNames = {"Create Login", "Create Add Features", "Create Reports", "Add Arrays"};
    private static final String[] developers = {"Mike Smith", "Edward Harrington", "Samantha Paulson", "Glenda Oberholzer"};
    private static final String[] taskIDs = {"CR:0:ITH", "CR:1:TON", "CR:2:SON", "CR:3:ZER"};
    private static final String[] taskStatus = {"To Do", "Doing", "Done", "To Do"};
    private static final int[] taskDurations = {5, 8, 2, 11};
    private static final int[] taskNumbers = {0, 1, 2, 3};
    private static final String[] taskDescriptions = {"Adding login feature", "Adding task features", "Adding reports to app", "Adding details to arrays"};
    
    public static void populateArrays(){
        LoginPage.setTaskNames(taskNames);
        LoginPage.setTaskDescriptions(taskDescriptions);
        LoginPage.setDeveloperNames(developers);
        LoginPage.setTaskDurations(taskDurations);
        LoginPage.setTaskStatuses(taskStatus);
        LoginPage.setTaskIDs(taskIDs);
        LoginPage.setTaskNumbers(taskNumbers);
    }
    
    public TaskClassTest(){
        populateArrays();
    }
    
    //testing if the task duration hours add up correctly 
    @Test
    public void testTotalTaskHours(){
     int totalHours = 0;
     int [] hours = {8, 10};
     
     for(int i: hours){
         totalHours = Tasks.returnTotalTaskHours(i);
        }
        
     assertEquals(18, totalHours);
    }
    
    //testing if more task duration hours adds up correctly
    @Test
    public void testMoreTotalTaskHours(){
     Tasks.setTotalHours(0);
     int totalHours = 0;
     int [] hours = {10,12,55,11,1};
     
     for(int i: hours){
         totalHours = Tasks.returnTotalTaskHours(i);
        }
     assertEquals(89, totalHours);
    }
    
    //testing if the correct message is displayed when the task description is less than 50 characters
    @Test
    public void taskDescriptionIsLessThanFifty(){
     String expectedMessage = "Task successfully captured";
     assertEquals(expectedMessage, Tasks.messageForTaskDescription("Adding features to welcome page."));
    }
    
    //testing if the correct message is displayed when the task description is less than 50 characters
    @Test
    public void taskDescriptionIsMoreThanFifty(){
     String expectedMessage = "Please enter a task description of less than 50 characters";
     assertEquals(expectedMessage, Tasks.messageForTaskDescription("Testing if the description is going to be more than 50 characters long."));
    }
    
    @Test
    public void TaskIDIsFormattedCorrectly(){
     //incrementing the task number to 1
     for(int i =0; i <2; i++){
         Tasks.createTaskNumber();
        }
        
        String expectedMessage = "AD:1:ITH";
        assertEquals(expectedMessage, Tasks.createTaskID("Add Task Feature", "Mike Smith"));
    }
    
    @Test
    public void TaskIDIsFormattedCorrectly2(){
     String taskName = "Create welcome animations";
     String [] [] expectedNactaul = { {"CR:0:IKE", "CR:1:ARD", "CR:2:THA", "CR:3:ND"}, {"John Mike", "andie bard", "May antha", "Billie Nd"} };
         
        for(int i =0; i<expectedNactaul.length-4; ++i){
            for(int j=0; j < expectedNactaul[i].length; ++j){
                 Tasks.createTaskNumber();
                 assertEquals(expectedNactaul[i], Tasks.createTaskID(taskName, expectedNactaul[i][j]));
            }
        }
    }    
    
    //Task 3 begins here
    //Code derravide from vcsoit prog5121 unit test youtube playlist
    @Test
    public void developerArrayPopulatesCorrectly(){ 
    String [] actualNames = LoginPage.getDeveloperNames();
        for (int i=0; i<developers.length; i++) {
                assertEquals(developers[i], actualNames[i]);    
        }
    }
    
    @Test
    public void highestDurationFoundIsTrue(){
      String expectedMessage = "Developer name: Glenda Oberholzer\nTask name: Add Arrays\nTask duration: 11";
      String actualMessage = Tasks.findHighestDuration();
      assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void taskNameFoundIsTrue(){
     String searchTerm = "Create Login";
     String expectedMessage = "Mike Smith, Create Login";
     String actualMessage = Tasks.findArrayUsingName(searchTerm);
     assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void arrayDeletesCorrectly(){
     String searchTerm = "Create Reports";
     String actualMessage = Tasks.deleteArray(searchTerm);
     String expectedMessage = "Entry " + "\""+ searchTerm + "\"" + " successfully deleted";
     assertEquals(expectedMessage, actualMessage);
    }
    
    @Test
    public void developerHasTasksIsTrue(){
     String searchTerm = "Samantha Paulson";
     String actualMessage = Tasks.findElementsUsingDeveloper(searchTerm);
     String expectedMessage = "Create Reports";
     assertEquals(expectedMessage, actualMessage);  
    }
    
    @Test
    public void testIfAllTasksPrintCorrectly(){
     String actaulMessage = Tasks.printAllArrays();
     String expectedMessage = "Successfully displayed";
     assertEquals(expectedMessage, actaulMessage);
    }  
}
