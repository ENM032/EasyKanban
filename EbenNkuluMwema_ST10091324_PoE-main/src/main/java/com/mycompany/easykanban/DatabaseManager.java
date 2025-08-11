package com.mycompany.easykanban;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                user_id INT NOT NULL,
                task_name VARCHAR(255) NOT NULL,
                task_description TEXT,
                developer_details VARCHAR(255),
                task_duration INT,
                task_status VARCHAR(50) DEFAULT 'To Do',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id)
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
}