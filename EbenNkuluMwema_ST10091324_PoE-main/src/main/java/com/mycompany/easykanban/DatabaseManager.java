package com.mycompany.easykanban;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;

/**
 * Database manager for handling user authentication and data persistence
 * Uses H2 embedded database with BCrypt password hashing
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:./data/easykanban;AUTO_SERVER=TRUE";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {
        initializeDatabase();
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Initialize database connection and create tables if they don't exist
     */
    private void initializeDatabase() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTables();
            LOGGER.info("Database initialized successfully");
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Create necessary tables for the application
     */
    private void createTables() throws SQLException {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) UNIQUE NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        String createTasksTable = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INT AUTO_INCREMENT PRIMARY KEY,
                task_name VARCHAR(255) NOT NULL,
                task_description TEXT,
                developer_name VARCHAR(255),
                task_duration INT,
                task_status VARCHAR(50) DEFAULT 'To Do',
                task_id VARCHAR(50),
                task_number INT,
                username VARCHAR(50),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createTasksTable);
            LOGGER.info("Database tables created successfully");
        }
    }
    
    /**
     * Hash password using BCrypt
     */
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    
    /**
     * Verify password against hash
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
    /**
     * Register a new user with hashed password
     */
    public boolean registerUser(String username, String password, String firstName, String lastName) {
        String sql = "INSERT INTO users (username, password_hash, first_name, last_name) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(password));
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            
            int rowsAffected = pstmt.executeUpdate();
            LOGGER.info("User registered successfully: " + username);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to register user: " + username, e);
            return false;
        }
    }
    
    /**
     * Authenticate user login
     */
    public User authenticateUser(String username, String password) {
        String sql = "SELECT id, username, password_hash, first_name, last_name FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if (verifyPassword(password, storedHash)) {
                        return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Authentication failed for user: " + username, e);
        }
        
        return null;
    }
    
    /**
     * Check if username already exists
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to check username existence: " + username, e);
        }
        
        return false;
    }
    
    /**
     * Get database connection for other operations
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOGGER.info("Database connection closed");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Failed to close database connection", e);
        }
    }
    
    // Task management methods
    public boolean createTask(String taskName, String taskDescription, String developerName, 
                             int taskDuration, String taskStatus, String taskId, int taskNumber, String username) {
        String sql = "INSERT INTO tasks (task_name, task_description, developer_name, task_duration, " +
                    "task_status, task_id, task_number, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, taskName);
            pstmt.setString(2, taskDescription);
            pstmt.setString(3, developerName);
            pstmt.setInt(4, taskDuration);
            pstmt.setString(5, taskStatus);
            pstmt.setString(6, taskId);
            pstmt.setInt(7, taskNumber);
            pstmt.setString(8, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating task: " + e.getMessage(), e);
            return false;
        }
    }
    
    public List<Task> getAllTasks(String username) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE username = ? ORDER BY task_number";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Task task = new Task(
                    rs.getInt("id"),
                    rs.getString("task_name"),
                    rs.getString("task_description"),
                    rs.getString("developer_name"),
                    rs.getInt("task_duration"),
                    rs.getString("task_status"),
                    rs.getString("task_id"),
                    rs.getInt("task_number"),
                    rs.getString("username")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving tasks: " + e.getMessage(), e);
        }
        
        return tasks;
    }
    
    public boolean updateTaskStatus(String taskId, String newStatus, String username) {
        String sql = "UPDATE tasks SET task_status = ? WHERE task_id = ? AND username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, taskId);
            pstmt.setString(3, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating task status: " + e.getMessage(), e);
            return false;
        }
    }
    
    public boolean deleteTask(String taskName, String username) {
        String sql = "DELETE FROM tasks WHERE task_name = ? AND username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, taskName);
            pstmt.setString(2, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting task: " + e.getMessage(), e);
            return false;
        }
    }
    
    public List<Task> getTasksByStatus(String status, String username) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE task_status = ? AND username = ? ORDER BY task_number";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Task task = new Task(
                    rs.getInt("id"),
                    rs.getString("task_name"),
                    rs.getString("task_description"),
                    rs.getString("developer_name"),
                    rs.getInt("task_duration"),
                    rs.getString("task_status"),
                    rs.getString("task_id"),
                    rs.getInt("task_number"),
                    rs.getString("username")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving tasks by status: " + e.getMessage(), e);
        }
        
        return tasks;
    }
    
    public Task getTaskWithHighestDuration(String username) {
        String sql = "SELECT * FROM tasks WHERE username = ? ORDER BY task_duration DESC LIMIT 1";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Task(
                    rs.getInt("id"),
                    rs.getString("task_name"),
                    rs.getString("task_description"),
                    rs.getString("developer_name"),
                    rs.getInt("task_duration"),
                    rs.getString("task_status"),
                    rs.getString("task_id"),
                    rs.getInt("task_number"),
                    rs.getString("username")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error finding task with highest duration: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    public List<Task> searchTasksByName(String taskName, String username) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE task_name = ? AND username = ? ORDER BY task_number";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, taskName);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Task task = new Task(
                    rs.getInt("id"),
                    rs.getString("task_name"),
                    rs.getString("task_description"),
                    rs.getString("developer_name"),
                    rs.getInt("task_duration"),
                    rs.getString("task_status"),
                    rs.getString("task_id"),
                    rs.getInt("task_number"),
                    rs.getString("username")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching tasks by name: " + e.getMessage(), e);
        }
        
        return tasks;
    }
    
    public List<Task> searchTasksByDeveloper(String developerName, String username) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE developer_name = ? AND username = ? ORDER BY task_number";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, developerName);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Task task = new Task(
                    rs.getInt("id"),
                    rs.getString("task_name"),
                    rs.getString("task_description"),
                    rs.getString("developer_name"),
                    rs.getInt("task_duration"),
                    rs.getString("task_status"),
                    rs.getString("task_id"),
                    rs.getInt("task_number"),
                    rs.getString("username")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching tasks by developer: " + e.getMessage(), e);
        }
        
        return tasks;
    }
    
    public int getTotalTaskHours(String username) {
        String sql = "SELECT SUM(task_duration) as total_hours FROM tasks WHERE username = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total_hours");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error calculating total task hours: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    /**
     * User data class
     */
    public static class User {
        private final int id;
        private final String username;
        private final String firstName;
        private final String lastName;
        
        public User(int id, String username, String firstName, String lastName) {
            this.id = id;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
        }
        
        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
    }
    
    /**
     * Task data class
     */
    public static class Task {
        private int id;
        private String taskName;
        private String taskDescription;
        private String developerName;
        private int taskDuration;
        private String taskStatus;
        private String taskId;
        private int taskNumber;
        private String username;
        
        public Task(int id, String taskName, String taskDescription, String developerName,
                   int taskDuration, String taskStatus, String taskId, int taskNumber, String username) {
            this.id = id;
            this.taskName = taskName;
            this.taskDescription = taskDescription;
            this.developerName = developerName;
            this.taskDuration = taskDuration;
            this.taskStatus = taskStatus;
            this.taskId = taskId;
            this.taskNumber = taskNumber;
            this.username = username;
        }
        
        // Getters
        public int getId() { return id; }
        public String getTaskName() { return taskName; }
        public String getTaskDescription() { return taskDescription; }
        public String getDeveloperName() { return developerName; }
        public int getTaskDuration() { return taskDuration; }
        public String getTaskStatus() { return taskStatus; }
        public String getTaskId() { return taskId; }
        public int getTaskNumber() { return taskNumber; }
        public String getUsername() { return username; }
        
        // Setters
        public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }
        public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
        public void setTaskDuration(int taskDuration) { this.taskDuration = taskDuration; }
    }
}