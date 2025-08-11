/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.easykanban;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ebenm
 */
public class Tasks {
 //initialising the variables below to use in other methods
 private static int totalTaskHours = 0;
 private static int taskNumber = 0;
 private static final AtomicInteger TASK_NUMBER = new AtomicInteger(0);
 private static final Logger LOGGER = Logger.getLogger(Tasks.class.getName());
 private static DatabaseManager dbManager;
 
 static {
     try {
         dbManager = DatabaseManager.getInstance();
     } catch (Exception e) {
         LOGGER.severe("Failed to initialize DatabaseManager: " + e.getMessage());
     }
 }
 
   
    //generating a task number and incrementing it everytime the method is called
    public static void createTaskNumber(){
     taskNumber = TASK_NUMBER.getAndIncrement();
    }
    
    //returning the generated task number
    public static int getTaskNumber(){
     return taskNumber;
    }
    
    //verifying whether or not the task description is less than 50 characters
    //if it is a true value is returned, and if it's not a false value is returned
    public static Boolean checkTaskDescription(String description){
     return description.length() <= 50;
    }
    
    //returning and displaying a suitable message if the task description is less than 50 characters or if its over 50 characters too long 
    public static String messageForTaskDescription(String description){
     String displayMessage;
        if(checkTaskDescription(description)){
            displayMessage = "Task successfully captured";
            JOptionPane.showMessageDialog(null, displayMessage, "Description captured", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            displayMessage = "Please enter a task description of less than 50 characters";
            JOptionPane.showMessageDialog(null, displayMessage, "Description too long", JOptionPane.ERROR_MESSAGE);
        }
        return displayMessage;
    }
    
    /*autogenerates a taskID by taking the first two letters of the task name,
    the task number and the last three letters of the developer full name,
    a semi colon gets added in between each task elements
    */
    public static String createTaskID(String taskName, String devDetails){  
     String firstTwoLetters = taskName.substring(0, 2) ;
     String devName = devDetails.trim();
     String lastThreeLetters = devName.substring(devName.length()-3, devName.length());
        return firstTwoLetters.toUpperCase() + ":" + getTaskNumber() + ":" + lastThreeLetters.toUpperCase();
    }
    
    //returns the full details of the tasks using the details entered by the user
    public static String printTaskDetails(String taskStatus, String developerDetails, int taskNumber, String taskName, String taskDescription, String taskID, int taskDuration){
        return "Task Status: " + taskStatus
                                             +"\nDeveloper Details: " + developerDetails
                                             +"\nTask Number: " + taskNumber
                                             +"\nTask Name: " + taskName
                                             +"\nTask Description: " + taskDescription
                                             +"\nTask ID: " + taskID
                                             +"\nTask Duration: " + taskDuration + " Hours";
    }
    
    //a setter method to reset the total hours in the unit tests
    public static void setTotalHours(int hours){
        totalTaskHours = hours;
    }
    
    //returning the total amount of hours for all the tasks
    public static int returnTotalTaskHours(int hours){
        totalTaskHours += hours;
        return totalTaskHours;
    }
    
    // New database-integrated methods
    public static boolean createTaskInDatabase(String taskName, String taskDescription, String developerName, 
                                             int taskDuration, String taskStatus, String username) {
        if (dbManager == null) {
            LOGGER.severe("DatabaseManager not initialized");
            return false;
        }
        
        createTaskNumber();
        String taskId = createTaskID(taskName, developerName);
        
        return dbManager.createTask(taskName, taskDescription, developerName, 
                                   taskDuration, taskStatus, taskId, getTaskNumber(), username);
    }
    
    public static int getTotalTaskHoursFromDatabase(String username) {
        if (dbManager == null) {
            LOGGER.severe("DatabaseManager not initialized");
            return 0;
        }
        
        return dbManager.getTotalTaskHours(username);
    }
    
    public static String findHighestDurationFromDatabase(String username) {
        if (dbManager == null) {
            LOGGER.severe("DatabaseManager not initialized");
            return "Database not available";
        }
        
        try {
            DatabaseManager.Task task = dbManager.getTaskWithHighestDuration(username);
            if (task != null) {
                String message = "Developer name: " + task.getDeveloperName() + 
                               "\nTask name: " + task.getTaskName() + 
                               "\nTask duration: " + task.getTaskDuration();
                
                JOptionPane.showMessageDialog(null, message, 
                    "Task details for the highest duration", JOptionPane.INFORMATION_MESSAGE);
                
                return message;
            } else {
                JOptionPane.showMessageDialog(null, "No tasks found", 
                    "No tasks available", JOptionPane.INFORMATION_MESSAGE);
                return "No tasks found";
            }
        } catch (Exception e) {
            LOGGER.severe("Error finding highest duration task: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Could not find the highest duration\nDatabase error occurred", 
                "Something went wrong!", JOptionPane.ERROR_MESSAGE);
            return "Duration not found";
        }
    }
    
    public static void displayCompletedTasksFromDatabase(String username) {
        if (dbManager == null) {
            LOGGER.severe("DatabaseManager not initialized");
            return;
        }
        
        try {
            List<DatabaseManager.Task> completedTasks = dbManager.getTasksByStatus("Done", username);
            
            if (completedTasks.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No completed tasks were found", 
                    "No completed tasks", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            StringBuilder developers = new StringBuilder();
            StringBuilder taskNames = new StringBuilder();
            StringBuilder taskDurations = new StringBuilder();
            
            for (DatabaseManager.Task task : completedTasks) {
                developers.append(task.getDeveloperName()).append(", ");
                taskNames.append(task.getTaskName()).append(", ");
                taskDurations.append(task.getTaskDuration()).append(", ");
            }
            
            // Remove trailing comma and space
            if (developers.length() > 2) {
                developers.setLength(developers.length() - 2);
                taskNames.setLength(taskNames.length() - 2);
                taskDurations.setLength(taskDurations.length() - 2);
            }
            
            UITheme.showSuccessMessage(
                "Developer names: " + developers + 
                "\nTask names: " + taskNames + 
                "\nTask durations: " + taskDurations, 
                "All completed tasks");
                
        } catch (Exception e) {
            LOGGER.severe("Error displaying completed tasks: " + e.getMessage());
            UITheme.showErrorMessage("Database error occurred", 
                "Something went wrong!");
        }
    }
    
    public static String findTaskByNameFromDatabase(String taskName, String username) {
        if (dbManager == null) {
            LOGGER.severe("DatabaseManager not initialized");
            return "Database not available";
        }
        
        try {
            List<DatabaseManager.Task> tasks = dbManager.searchTasksByName(taskName, username);
            
            if (tasks.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Could not find task name : \"" + taskName + "\"\ncheck spelling of task or add task details", 
                    "Something went wrong!", JOptionPane.ERROR_MESSAGE);
                return "Nothing found";
            }
            
            StringBuilder taskNames = new StringBuilder();
            StringBuilder developerNames = new StringBuilder();
            StringBuilder taskStatuses = new StringBuilder();
            
            for (int i = 0; i < tasks.size(); i++) {
                DatabaseManager.Task task = tasks.get(i);
                if (i > 0) {
                    taskNames.append(", ");
                    developerNames.append(", ");
                    taskStatuses.append(", ");
                }
                taskNames.append(task.getTaskName());
                developerNames.append(task.getDeveloperName());
                taskStatuses.append(task.getTaskStatus());
            }
            
            UITheme.showSuccessMessage(
                "Task name: " + taskNames + 
                "\nDeveloper name: " + developerNames + 
                "\nTask status: " + taskStatuses, 
                "Task details found");
                
            return developerNames.toString().trim() + ", " + taskNames.toString().trim();
            
        } catch (Exception e) {
            LOGGER.severe("Error finding task by name: " + e.getMessage());
            UITheme.showErrorMessage("Database error occurred", 
                "Something went wrong!");
            return "Nothing found";
        }
    }
    
    public static String findTasksByDeveloperFromDatabase(String developerName, String username) {
        if (dbManager == null) {
            LOGGER.severe("DatabaseManager not initialized");
            return "Database not available";
        }
        
        try {
            List<DatabaseManager.Task> tasks = dbManager.searchTasksByDeveloper(developerName, username);
            
            if (tasks.isEmpty()) {
                UITheme.showErrorMessage(
                    "Could not find any tasks associated to: \"" + developerName + "\"", 
                    "Something went wrong!");
                return "Nothing found";
            }
            
            StringBuilder taskNames = new StringBuilder();
            
            for (int i = 0; i < tasks.size(); i++) {
                if (i > 0) {
                    taskNames.append(", ");
                }
                taskNames.append(tasks.get(i).getTaskName());
            }
            
            UITheme.showSuccessMessage(
                "Task name/s found: \n" + taskNames, 
                "Developer task details found");
                
            return taskNames.toString().trim();
            
        } catch (Exception e) {
            LOGGER.severe("Error finding tasks by developer: " + e.getMessage());
            UITheme.showErrorMessage("Database error occurred", 
                "Something went wrong!");
            return "Nothing found";
        }
    }
    
    public static String deleteTaskFromDatabase(String taskName, String username) {
        if (dbManager == null) {
            LOGGER.severe("DatabaseManager not initialized");
            return "Database not available";
        }
        
        try {
            boolean deleted = dbManager.deleteTask(taskName, username);
            
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Task successfully deleted", 
                    "Deletion of task", JOptionPane.INFORMATION_MESSAGE);
                return "Entry \"" + taskName + "\" successfully deleted";
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No task has been deleted\ntask: \"" + taskName + "\" does not exist", 
                    "Something went wrong!", JOptionPane.ERROR_MESSAGE);
                return "Nothing deleted";
            }
            
        } catch (Exception e) {
            LOGGER.severe("Error deleting task: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database error occurred", 
                "Something went wrong!", JOptionPane.ERROR_MESSAGE);
            return "Nothing deleted";
        }
    }
    
    public static String displayAllTasksFromDatabase(String username) {
        if (dbManager == null) {
            LOGGER.severe("DatabaseManager not initialized");
            return "Database not available";
        }
        
        try {
            List<DatabaseManager.Task> tasks = dbManager.getAllTasks(username);
            
            if (tasks.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "No tasks details available to display, please add task details first", 
                    "No tasks available", JOptionPane.INFORMATION_MESSAGE);
                return "Not displayed";
            }
            
            StringBuilder taskStatuses = new StringBuilder();
            StringBuilder developers = new StringBuilder();
            StringBuilder taskNumbers = new StringBuilder();
            StringBuilder taskNames = new StringBuilder();
            StringBuilder taskDescriptions = new StringBuilder();
            StringBuilder taskIDs = new StringBuilder();
            StringBuilder taskDurations = new StringBuilder();
            
            for (DatabaseManager.Task task : tasks) {
                taskStatuses.append(task.getTaskStatus()).append(", ");
                developers.append(task.getDeveloperName()).append(", ");
                taskNumbers.append(task.getTaskNumber()).append(", ");
                taskNames.append(task.getTaskName()).append(", ");
                taskDescriptions.append(task.getTaskDescription()).append(", ");
                taskIDs.append(task.getTaskId()).append(", ");
                taskDurations.append(task.getTaskDuration()).append(", ");
            }
            
            // Remove trailing comma and space
            if (taskStatuses.length() > 2) {
                taskStatuses.setLength(taskStatuses.length() - 2);
                developers.setLength(developers.length() - 2);
                taskNumbers.setLength(taskNumbers.length() - 2);
                taskNames.setLength(taskNames.length() - 2);
                taskDescriptions.setLength(taskDescriptions.length() - 2);
                taskIDs.setLength(taskIDs.length() - 2);
                taskDurations.setLength(taskDurations.length() - 2);
            }
            
            JOptionPane.showMessageDialog(null, 
                "All task Statuses: " + taskStatuses + 
                "\nAll developer names: " + developers +
                "\nAll task numbers: " + taskNumbers + 
                "\nAll task names: " + taskNames + 
                "\nAll task descriptions: " + taskDescriptions +
                "\nAll task IDs: " + taskIDs + 
                "\nAll task durations: " + taskDurations, 
                "Final task summary", JOptionPane.INFORMATION_MESSAGE);
                
            return "Successfully displayed";
            
        } catch (Exception e) {
            LOGGER.severe("Error displaying all tasks: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database error occurred", 
                "Something went wrong!", JOptionPane.ERROR_MESSAGE);
            return "Not displayed";
        }
    }
    
    //finds the task with the highest duration in the task durations array
    public static String findHighestDuration(){
     String message;
     
     try{
         int highestDuration;
         int arrayIndex = 0;
         String[] taskNamesArray = LoginPage.getTaskNames();
         String[] developerArray = LoginPage.getDeveloperNames();
         int[] taskDurationsArray = LoginPage.getTaskDurations();
         highestDuration = taskDurationsArray[0];
         
         for(int i=0; i < taskDurationsArray.length; i++){
              if(taskDurationsArray[i] > highestDuration){
                  highestDuration = taskDurationsArray[i];
                  arrayIndex = i;
                }
            } 
         UITheme.showSuccessMessage("Developer name: " + developerArray[arrayIndex] +"\nTask name: " + 
                                      taskNamesArray[arrayIndex], "Task details for the highest duration");
         
         message = "Developer name: " + developerArray[arrayIndex] +"\nTask name: " + taskNamesArray[arrayIndex] +
                    "\nTask duration: " + taskDurationsArray[arrayIndex];
     }
     catch(IndexOutOfBoundsException | NullPointerException ioobeNnpe){
             UITheme.showErrorMessage("Could not find the highest duration\n" +
                                           "Add task details first", "Something went wrong!");  
           
             message = "Duration not found";
     }
     return message;
    }
    
    //linearly searching through for arrays
    public static ArrayList<Integer> searchForTask(String [] anyTaskArray, String lookForValue){
     ArrayList<Integer> indexesArray = new ArrayList<>();
     
     for(int i =0;i < anyTaskArray.length; i++){
         if(anyTaskArray[i].equals(lookForValue)){
             indexesArray.add(i);
            }    
        } 
     return indexesArray;
    }
    //displays all task with the status "done" using a linear search algorithm
    public static void displayCompletedTasks(String lookForValue){
     try{
         StringBuilder developers = new StringBuilder();
         StringBuilder taskNames = new StringBuilder();
         StringBuilder taskDurations = new StringBuilder();
         String[] taskNamesArray = LoginPage.getTaskNames();
         String[] taskStatusArray = LoginPage.getTaskStatuses();
         String[] developerArray = LoginPage.getDeveloperNames();
         int[] taskDurationsArray = LoginPage.getTaskDurations();
         ArrayList<Integer> indexes = searchForTask(taskStatusArray, lookForValue);

         for(int i=0; i<indexes.size();i++){
              developers.append(developerArray[indexes.get(i)]).append(", ");
              taskNames.append(taskNamesArray[indexes.get(i)]).append(", ");
              taskDurations.append(taskDurationsArray[indexes.get(i)]).append(", ");
            }
         //deletes the comma and whitespace at the end of each stringbuilder
         developers.deleteCharAt(developers.length()-2);
         taskNames.deleteCharAt(taskNames.length()-2);
         taskDurations.deleteCharAt(taskDurations.length()-2);
         
         UITheme.showSuccessMessage("Developer names: " + developers + "\nTask names: " + taskNames +
                                        "\nTask durations: " + taskDurations, "All completed tasks");
        }
        catch(IndexOutOfBoundsException | NullPointerException ioobeNnpe){
             UITheme.showErrorMessage("No completed tasks were found" +
                                          "\nadd completed task details first", "Something went wrong!");
        }    
    }
    
    //finds and displays elements from specific arrays using a linear search algorithm
    public static String findArrayUsingName(String lookForValue){
     String message;
        
     try{
         StringBuilder taskNames = new StringBuilder();
         StringBuilder developerNames = new StringBuilder();
         StringBuilder taskStatuses = new StringBuilder();
         String[] taskNamesArray = LoginPage.getTaskNames(); 
         String[] developerArray = LoginPage.getDeveloperNames();
         String[] taskStatusArray = LoginPage.getTaskStatuses();
         ArrayList<Integer> indexes = searchForTask(taskNamesArray, lookForValue);

         for(int i=0; i<indexes.size(); i++){
              taskNames.append(taskNamesArray[indexes.get(i)]).append(", ");
              developerNames.append(developerArray[indexes.get(i)]).append(", ");
              taskStatuses.append(taskStatusArray[indexes.get(i)]).append(", ");
            }
         //deletes the comma and whitespace at the end of each stringbuilder 
         taskNames.deleteCharAt(taskNames.toString().length()-2);
         developerNames.deleteCharAt(developerNames.length()-2);
         taskStatuses.deleteCharAt(taskStatuses.length()-2);
          
         UITheme.showSuccessMessage("Task name: " + taskNames +
                                       "\nDeveloper name: " + developerNames + 
                                       "\nTask status: " + taskStatuses, "Task details found");
         message = developerNames.toString().trim() + ", " + taskNames.toString().trim();
        }
        catch(IndexOutOfBoundsException | NullPointerException ioobeNnpe){
             UITheme.showErrorMessage("Could not find task name : " + "\""+ lookForValue + "\"" + 
                                          "\ncheck spelling of task or add task details", "Something went wrong!");
             message = "Nothing found";
        }
     return message;
    }
    
    //finds and displays elements from specific arrays using a linear search algorithm
    public static String findElementsUsingDeveloper(String lookForValue){
     String message;
     
     try{
         String[] developerArray = LoginPage.getDeveloperNames();
         ArrayList<Integer> indexes = searchForTask(developerArray, lookForValue);
         StringBuilder taskNames = new StringBuilder();
         String[] taskNamesArray = LoginPage.getTaskNames();   
           
         for(int i=0; i<indexes.size();i++){
              taskNames.append(taskNamesArray[indexes.get(i)]).append(", ");
            }
            
        //deletes the comma and whitespace at the end of each stringbuilder 
        taskNames.deleteCharAt(taskNames.length()-2);
        UITheme.showSuccessMessage("Task name/s found: \n" + taskNames, 
                                          "Developer task details found");
        message = taskNames.toString().trim();
        }
        catch(IndexOutOfBoundsException | NullPointerException ioobeNnpe){
               UITheme.showErrorMessage("Could not find any tasks associated to: " + "\""+ lookForValue + "\"", "Something went wrong!");
               message = "Nothing found";
        }
     return message;
    }
    
    //converts the int arrays to Integer arrayLists
    public static ArrayList<Integer> convertPrimitiveArrays(int [] anyArray){
     ArrayList<Integer> newArrayList = new ArrayList<>();
        for(int i=0; i<anyArray.length; i++){
            newArrayList.add(anyArray[i]);
        }
     return newArrayList;
    }
    
    //deletes specific elements inside various arrays using a linear search algorithm
    public static String deleteArray(String lookForValue){
     String message;
     try{
         String[] taskNamesArray = LoginPage.getTaskNames();  
         ArrayList<Integer> indexes = searchForTask(taskNamesArray, lookForValue);
         int[] taskDurationsArray = LoginPage.getTaskDurations();
         int[] taskNumbersArray = LoginPage.getTaskNumbers(); 
         String[] developerArray = LoginPage.getDeveloperNames();
         String[] taskIDsArray = LoginPage.getTaskIDs();
         String[] taskStatusArray = LoginPage.getTaskStatuses();
         String[] taskDescriptionsArray = LoginPage.getTaskDescriptions();
           
         //converting the retrieved arrays to arrayList to easily delete specific elements 
         ArrayList<String> taskNameList = new ArrayList<>(Arrays.asList(taskNamesArray));
         ArrayList<String> taskDescriptionList = new ArrayList<>(Arrays.asList(taskDescriptionsArray));
         ArrayList<String> developerList = new ArrayList<>(Arrays.asList(developerArray));
         ArrayList<String> taskIDList = new ArrayList<>(Arrays.asList(taskIDsArray));
         ArrayList<String> taskStatusList = new ArrayList<>(Arrays.asList(taskStatusArray));
         ArrayList<Integer> taskNumberList = convertPrimitiveArrays(taskNumbersArray);
         ArrayList<Integer> taskDurationList = convertPrimitiveArrays(taskDurationsArray);
           
            for(int i=indexes.size()-1; i >=0; i--){
                 taskNameList.remove((int)indexes.get(i));
                 taskDescriptionList.remove((int)indexes.get(i));
                 developerList.remove((int)indexes.get(i));
                 taskIDList.remove((int)indexes.get(i));
                 taskStatusList.remove((int)indexes.get(i));
                 taskDurationList.remove(indexes.get(i));
                 taskNumberList.remove(indexes.get(i));
            }
            
             String[] names = new String[taskNameList.size()];
             String[] developers = new String[taskNameList.size()];
             String[] descriptions = new String[taskNameList.size()];
             String[] statuses = new String[taskNameList.size()];
             String[] ids = new String[taskNameList.size()];
             int[] numbers = new int[taskNameList.size()];
             int[] durations = new int[taskNameList.size()];
             
             //converting the updated arrayList back to arrays
             for(int j=0; j<taskNameList.size(); j++){
                 names[j] = taskNameList.get(j);
                 descriptions[j] = taskDescriptionList.get(j);
                 developers[j] = developerList.get(j);
                 ids[j] = taskIDList.get(j);
                 statuses[j] = taskStatusList.get(j);
                 durations[j] = taskDurationList.get(j);
                 numbers[j] = taskNumberList.get(j);
            }
             
            LoginPage.setTaskNames(names); 
            LoginPage.setTaskDescriptions(descriptions);
            LoginPage.setDeveloperNames(developers);
            LoginPage.setTaskIDs(ids);
            LoginPage.setTaskStatuses(statuses);
            LoginPage.setTaskDurations(durations);
            LoginPage.setTaskNumbers(numbers);
             
         UITheme.showSuccessMessage("Task/s successfully deleted", "Deletion of tasks");
            
         message = "Entry " + "\""+ lookForValue + "\"" + " successfully deleted";
        }
        catch(IndexOutOfBoundsException | NullPointerException ioobeNnpe){
             UITheme.showErrorMessage("No task/s have been deleted\ntask: " + "\""+ lookForValue + "\"" +
                                           " does not exist", "Something went wrong!");
            
             message = "Nothing deleted";
        }
     return message;
    }
    
    public static String printAllArrays(){
     String message;
     
     try{
          StringBuilder taskStatuses = new StringBuilder();
          StringBuilder developers = new StringBuilder();
          StringBuilder taskNumbers = new StringBuilder();
          StringBuilder taskNames = new StringBuilder();
          StringBuilder taskDescriptions = new StringBuilder();
          StringBuilder taskIDs = new StringBuilder();
          StringBuilder taskDurations = new StringBuilder();
          
          String[] statusArray = LoginPage.getTaskStatuses();
          String[] developersArray = LoginPage.getDeveloperNames();
          int[] numbersArray = LoginPage.getTaskNumbers();
          String[] namesArray = LoginPage.getTaskNames();
          String[] descriptionsArray = LoginPage.getTaskDescriptions();
          String[] idsArray = LoginPage.getTaskIDs();
          int[] durationsArray = LoginPage.getTaskDurations();
                  
          //adds each element from various arrays to various stringbuilders 
          for(int i=0; i<namesArray.length; i++){
               taskStatuses.append(statusArray[i]).append(", ");
               developers.append(developersArray[i]).append(", ");
               taskNumbers.append(numbersArray[i]).append(", ");
               taskNames.append(namesArray[i]).append(", ");
               taskDescriptions.append(descriptionsArray[i]).append(", ");
               taskIDs.append(idsArray[i]).append(", ");
               taskDurations.append(durationsArray[i]).append(", ");       
            }
         //deletes the comma and whitespace at the end of each stringbuilder
         taskStatuses.deleteCharAt(taskStatuses.length()-2);
         developers.deleteCharAt(developers.length()-2);
         taskNumbers.deleteCharAt(taskNumbers.length()-2);
         taskNames.deleteCharAt(taskNames.length()-2);
         taskDescriptions.deleteCharAt(taskDescriptions.length()-2);
         taskIDs.deleteCharAt(taskIDs.length()-2);
         taskDurations.deleteCharAt(taskDurations.length()-2);
            
         UITheme.showSuccessMessage("All task Statuses: " + taskStatuses + "\nAll developer names: " + developers +
                     "\nAll task numbers: " + taskNumbers + "\nAll task names: " + taskNames + "\nAll task descriptions: " + taskDescriptions +
                     "\nAll task IDs: " + taskIDs + "\nAll task durations: " + taskDurations, "Final task summary");
             
         message = "Successfully displayed";
     }
     catch(IndexOutOfBoundsException | NullPointerException ioobeNnpe){
            UITheme.showErrorMessage("No tasks details available to display, please add task details first", "Something went wrong!");
            message = "Not displayed";   
     }
     return message;
    }
}

