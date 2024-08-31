/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.easykanban;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author ebenm
 */
public class LoginPage extends javax.swing.JFrame {
  private static int taskDuration;  
  private static int totalHours;
  
  private final ImageIcon imgIcon = new ImageIcon("Login.png");
  
  private final static String comboBoxItems [] = {"To DO", "Doing", "Done"};
  private final static JComboBox comboBox = new JComboBox(comboBoxItems);    
  
  private static int Option = 0;
  private static int secondChoice = 0;
  
  private static String taskName;
  private static String taskDescription;
  private static String developerDetails;
  private static String taskStatus;
  private static String taskID;
  private static int taskNumber;
  
  protected static String [] taskNames;
  protected static String [] taskDescriptions;
  protected static String [] developerNames;
  protected static String [] taskStatuses;
  protected static String [] taskIDs;
  protected static int [] taskDurations;
  protected static int [] taskNumbers;
  
    //Creates new form LoginPage
    public LoginPage() {
        initComponents();
        
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        
        menuLabel.setText("  ");
        menuLabel.setIcon(imgIcon);
        
        loginButton.setFocusable(false);
        resetButton.setFocusable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginPanel = new javax.swing.JPanel();
        loginButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        usernameText = new javax.swing.JTextField();
        passwordText = new javax.swing.JPasswordField();
        headerLabel = new javax.swing.JLabel();
        registrationLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pageSelector = new javax.swing.JPanel();
        menuLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EasyKanban Login");
        setBackground(new java.awt.Color(255, 255, 255));
        setName("frame"); // NOI18N

        loginPanel.setBackground(new java.awt.Color(165, 122, 255));
        loginPanel.setMaximumSize(new java.awt.Dimension(435, 385));

        loginButton.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        loginButton.setText("Login");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        resetButton.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        usernameLabel.setForeground(new java.awt.Color(0, 0, 0));
        usernameLabel.setText("Username:");

        passwordLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        passwordLabel.setForeground(new java.awt.Color(0, 0, 0));
        passwordLabel.setText("Password:");

        usernameText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        passwordText.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        headerLabel.setFont(new java.awt.Font("Segoe UI", 3, 26)); // NOI18N
        headerLabel.setText("Welcome back!");

        registrationLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        registrationLabel.setText("Don't have an account? Sign Up HERE");
        registrationLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseLabelClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Log In to use EasyKanban services");

        javax.swing.GroupLayout loginPanelLayout = new javax.swing.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginPanelLayout.createSequentialGroup()
                        .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, loginPanelLayout.createSequentialGroup()
                                    .addComponent(usernameLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, loginPanelLayout.createSequentialGroup()
                                    .addComponent(passwordLabel)
                                    .addGap(26, 26, 26)
                                    .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(loginPanelLayout.createSequentialGroup()
                                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(loginPanelLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(registrationLabel)))
                        .addGap(27, 27, 27))))
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(headerLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginPanelLayout.createSequentialGroup()
                .addComponent(headerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(loginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(registrationLabel)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pageSelector.setBackground(new java.awt.Color(255, 255, 255));

        menuLabel.setBackground(new java.awt.Color(70, 130, 180));
        menuLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        menuLabel.setText("Icon here");

        javax.swing.GroupLayout pageSelectorLayout = new javax.swing.GroupLayout(pageSelector);
        pageSelector.setLayout(pageSelectorLayout);
        pageSelectorLayout.setHorizontalGroup(
            pageSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pageSelectorLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(menuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pageSelectorLayout.setVerticalGroup(
            pageSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pageSelectorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pageSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(loginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loginPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pageSelector, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    //creating a setter method to set the username in other methods 
    public static void setUsername(String Username){
        usernameText.setText(Username);
    }
    
    //creating a setter method to set the password in other methods
    public static void setPassword(String Password){
        passwordText.setText(Password);
    }
    
    public static void setTaskNames(String[] names){
        taskNames = names;
    }
    
    public static void setTaskDescriptions(String[] descriptions){
        taskDescriptions = descriptions;
    }
    
    public static void setDeveloperNames(String[] developers){
        developerNames = developers;
    }
    
    public static void setTaskDurations(int[] durations){
        taskDurations = durations;
    }
    
    public static void setTaskStatuses(String[] statuses){
        taskStatuses = statuses;
    }
    
    public static void setTaskNumbers(int[] numbers){
        taskNumbers = numbers;
    }
    
    public static void setTaskIDs(String[] ids){
        taskIDs = ids;
    }
    
    
    public static String getUsername(){
        return usernameText.getText();
    }
    
    public static String [] getTaskNames(){
        return taskNames;
    }
    
    public static String [] getTaskDescriptions(){
        return taskDescriptions;
    }
    
    public static String [] getDeveloperNames(){
        return developerNames;
    }
    
    public static int [] getTaskDurations(){
        return taskDurations;
    }
    
    public static String [] getTaskStatuses(){
        return taskStatuses;
    }
    
    public static int [] getTaskNumbers(){
        return taskNumbers;
    }
    
    public static String [] getTaskIDs(){
        return taskIDs;
    }
    
    //displays the gui to the user and populates the necessary task details in  various array
    private static void displayTaskOptions(){
    //text blocks caused multiple errors during github workflow, hence i removed them from the project
    Option = Integer.parseInt(JOptionPane.showInputDialog(null, "Welcome to EasyKanban\nPlease select one of the following options:" +
                                                         "\nTo add tasks - Type 1 and press enter\nTo show reports - Type 2 and press" +
                                                         " enter\nTo exit program - Type 3 and press enter",
                                                         "EasyKanban Welcome Page", JOptionPane.INFORMATION_MESSAGE));
                 
        switch (Option) {
        case 1:
                int numberOfTasks = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter number of tasks required", 
                                                                                 "Adding Kanban Tasks", JOptionPane.QUESTION_MESSAGE));
                taskNames = new String [numberOfTasks];
                taskDescriptions = new String [numberOfTasks];
                developerNames = new String [numberOfTasks];
                taskStatuses = new String [numberOfTasks];
                taskIDs = new String [numberOfTasks];
                taskDurations = new int [numberOfTasks];
                taskNumbers = new int [numberOfTasks];
                     
                for(int i=0; i<numberOfTasks; i++){
                    taskName = JOptionPane.showInputDialog(null, "Please enter a task name", "Adding Kanban Tasks", JOptionPane.QUESTION_MESSAGE);
                    
                    taskDescription = JOptionPane.showInputDialog(null, "Please describe the task in less than 50 words", 
                                                                  "Adding Kanban Tasks", JOptionPane.QUESTION_MESSAGE);
                    
                    developerDetails = JOptionPane.showInputDialog(null, "Please enter the name of the developer assigned to this task", 
                                                                   "Adding Kanban Tasks", JOptionPane.QUESTION_MESSAGE);
                    
                    taskDuration = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter the estimated duration of this task", 
                                                                                "Adding Kanban Tasks", JOptionPane.QUESTION_MESSAGE));
                    
                    comboBox.setSelectedIndex(-1);
                    JOptionPane.showMessageDialog(null, comboBox, "Select the status of this task", JOptionPane.QUESTION_MESSAGE);
                    taskStatus = (String) comboBox.getSelectedItem();
                    
                        
                    
                     
                    if(taskStatus == null){
                       JOptionPane.showMessageDialog(null, "Task status cannot be empty\nplease " +
                                                    "select a task status", "Something went wrong", JOptionPane.ERROR_MESSAGE); 
                    }
                    else{
                      if(Tasks.messageForTaskDescription(taskDescription).equals("Task successfully captured")){
                         totalHours = Tasks.returnTotalTaskHours(taskDuration);
                         Tasks.createTaskNumber();
                         taskID = Tasks.createTaskID(taskName, developerDetails);
                         taskNumber = Tasks.getTaskNumber();
                         JOptionPane.showMessageDialog(null, Tasks.printTaskDetails(taskStatus, developerDetails, 
                                                    taskNumber, taskName, taskDescription,
                                                    taskID, taskDuration), 
                                                    "Added Task Summary", JOptionPane.INFORMATION_MESSAGE); 
                         
                         taskNames[i] = taskName;
                         taskDescriptions[i] = taskDescription;
                         developerNames[i] = developerDetails;
                         taskStatuses[i] = taskStatus;
                         taskIDs[i] = taskID;
                         taskDurations[i] = taskDuration;
                         taskNumbers[i] = taskNumber;
                        }
                    } 
                }break;
        case 2:  
                //text blocks caused multiple errors during github workflow, hence i removed them from the project 
                secondChoice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please select one of the following options:" +
                                                "\n-To view all done task - Type 1 and press enter" +
                                                "\n-To view task with the highest duration - Type 2 and press enter" +
                                                "\n-To search for a task - Type 3 and press enter" +
                                                "\n-To view tasks assigned to a specific developer - Type 4 and press enter" +
                                                "\n-To delete a task - Type 5 and press enter" +
                                                "\n-To view all added tasks - Type 6 and press enter",
                                                "Viewing reports", JOptionPane.INFORMATION_MESSAGE));
                switch (secondChoice) {
                case 1:
                       Tasks.displayCompletedTasks("Done");
                break;
                case 2:
                       Tasks.findHighestDuration();
                break;
                case 3:
                       String searchTask = JOptionPane.showInputDialog(null, "Please enter a task name", "Find task/s", JOptionPane.INFORMATION_MESSAGE);
                       Tasks.findArrayUsingName(searchTask);
                break;
                case 4:
                       String developer = JOptionPane.showInputDialog(null, "Please enter the developer's name", "find developer task/s", JOptionPane.INFORMATION_MESSAGE);
                       Tasks.findElementsUsingDeveloper(developer);
                break;
                case 5:
                       String deleteTask = JOptionPane.showInputDialog(null, "Please enter a task name to delete task", "Deletion of task", JOptionPane.INFORMATION_MESSAGE);
                       Tasks.deleteArray(deleteTask);
                break;
                case 6:
                       Tasks.printAllArrays();
                break;
                default:
                        JOptionPane.showMessageDialog(null, "Invalid input entered", "Something went wrong!", JOptionPane.ERROR_MESSAGE);
                        break;
        }break;
        case 3: 
               Date currentDate = new Date();
               SimpleDateFormat hoursAndMinutes = new SimpleDateFormat("h:mm a");
               JOptionPane.showMessageDialog(null, "Successfully logged out of account:\n" + getUsername() + " at " +
                                              hoursAndMinutes.format(currentDate),"EasyKanban Session Ended", JOptionPane.INFORMATION_MESSAGE);
        break;
        default:
                //displaying a suitable message if the user does not enter either 1, 2 or 3 during the first JOptionPane
                JOptionPane.showMessageDialog(null, "Please enter one of the options\npreviously displayed to you", 
                                              "Invalid option entered", JOptionPane.ERROR_MESSAGE);
        break;    
        }
    }
    
    private static void displayTotalHours(){
    //displaying the total amount of hours after all the tasks have been added
      if(Option ==1 && taskStatus != null)
        JOptionPane.showMessageDialog(null, "Total amount of hours for all the entered tasks: " + totalHours +
                                      " hours", "Total amount of task hours", JOptionPane.INFORMATION_MESSAGE);           
    }  
    
    //validating whether or not the user's username and password exist in the text file when the login button is clicked
    //if it does, the user will be logged in. if it doesn't, a suitable message will be displayed
    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        if (evt.getSource() == loginButton){
             String username = usernameText.getText();
             String password = String.valueOf(passwordText.getPassword());
             
           if (!Login.loginStatus(username, password).equals("Username or password incorrect, please try again.")){
                 this.dispose();
                  while (Option !=3){
                    try{   
                        displayTaskOptions();
                    }
                    //Catching possible invalid inputs from the user and displaying a suitable error message     
                    catch(StringIndexOutOfBoundsException | NumberFormatException nfe){
                          JOptionPane.showMessageDialog(null, "Invalid input entered, please try again.", 
                                                        "Something went wrong", JOptionPane.ERROR_MESSAGE);
                    }
                    displayTotalHours();
                }
            }   
        }
    }//GEN-LAST:event_loginButtonActionPerformed
   
    //reseting the username text field, password text field and Label message when the reset button is clicked
    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:
        if(evt.getSource() == resetButton){
        usernameText.setText("");
        passwordText.setText("");
        }
    }//GEN-LAST:event_resetButtonActionPerformed
    
    //creating a new thread and displaying the registration page when the label is clicked
    private void mouseLabelClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseLabelClicked
        // TODO add your handling code here:
        if(evt.getSource() == registrationLabel){
           this.dispose();
            java.awt.EventQueue.invokeLater(() -> {
                  new RegistrationPage().setVisible(true);
            });
        }
    }//GEN-LAST:event_mouseLabelClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      //</editor-fold>
      /* Create and display the form */
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel headerLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel menuLabel;
    private javax.swing.JPanel pageSelector;
    private javax.swing.JLabel passwordLabel;
    protected static javax.swing.JPasswordField passwordText;
    private javax.swing.JLabel registrationLabel;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel usernameLabel;
    protected static javax.swing.JTextField usernameText;
    // End of variables declaration//GEN-END:variables
}
