# EasyKanban - Task Management System

A Java-based Kanban task management application with secure user authentication and database persistence.

## Features

- **Task Management**: Create, read, update, and delete tasks with full lifecycle management
- **Search & Filter**: Find tasks by name, developer, or status with advanced filtering
- **Time Tracking**: Track task duration and calculate total project hours
- **Security**: BCrypt password hashing and secure user authentication
- **Database**: H2 embedded database with automatic data persistence
- **User Isolation**: Each user has their own secure task workspace
- **Analytics**: Task duration analysis and completion reporting

## Prerequisites

Before running this application, ensure you have the following installed:

### Required Software
1. **Java Development Kit (JDK) 11 or higher**
   - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Verify installation: `java -version` and `javac -version`

2. **Apache Maven 3.6.0 or higher**
   - Download from: [Maven Official Website](https://maven.apache.org/download.cgi)
   - Installation guide: [Maven Installation Instructions](https://maven.apache.org/install.html)
   - Verify installation: `mvn -version`

3. **Git (Optional, for cloning)**
   - Download from: [Git Official Website](https://git-scm.com/downloads)
   - Verify installation: `git --version`

## Setup Instructions

### 1. Clone or Download the Repository

**Option A: Clone with Git**
```bash
git clone https://github.com/ENM032/EasyKanban.git
cd EasyKanban
```

**Option B: Download ZIP**
1. Download the ZIP file from the GitHub repository
2. Extract to your desired location
3. Navigate to the extracted folder

### 2. Verify Project Structure
Ensure your project structure looks like this:
```
easykanban_v2/
├── src/
│   ├── main/java/
│   └── test/java/
├── docs/
├── pom.xml
├── README.md
└── .gitignore
```

### 3. Install Dependencies
Run the following command in the project root directory:
```bash
mvn clean install
```

This will:
- Download all required dependencies
- Compile the source code
- Run tests
- Create the target directory with compiled classes

## Running the Application

### Method 1: Using Maven (Recommended)
```bash
# Compile and run the main application
mvn clean compile exec:java -Dexec.mainClass="com.mycompany.easykanban.EasyKanbanMain"
```

### Method 2: Using Java directly
```bash
# First, compile the project
mvn clean compile

# Then run the main class
java -cp target/classes com.mycompany.easykanban.EasyKanbanMain
```

### Method 3: Create and run JAR file
```bash
# Create executable JAR
mvn clean package

# Run the JAR file
java -jar target/easykanban-1.0-SNAPSHOT.jar
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=DatabaseManagerTest
mvn test -Dtest=LoginClassTest
mvn test -Dtest=TaskClassTest
```

### Generate Test Reports
```bash
mvn surefire-report:report
```
Test reports will be generated in `target/site/surefire-report.html`

## Development Setup

### IDE Configuration

**For IntelliJ IDEA:**
1. Open IntelliJ IDEA
2. Select "Open" and choose the project folder
3. IntelliJ will automatically detect the Maven project
4. Wait for dependency resolution to complete

**For Eclipse:**
1. Open Eclipse
2. File → Import → Existing Maven Projects
3. Browse to the project folder and select it
4. Click Finish

**For VS Code:**
1. Install the "Extension Pack for Java"
2. Open the project folder in VS Code
3. VS Code will automatically detect the Maven project

**For NetBeans:**
1. Open NetBeans IDE
2. File → Open Project
3. Navigate to the project folder and select it
4. NetBeans will automatically recognize the Maven project structure
5. Wait for dependency resolution to complete

**Note about NetBeans**: This project was originally developed in NetBeans IDE but has been migrated to a Maven-based structure for better cross-platform compatibility. All NetBeans-specific configuration files have been removed to ensure the project works seamlessly across different IDEs and build environments.

### Database Configuration

The application uses H2 embedded database:
- **Database files**: Created automatically in the `data/` directory
- **Connection URL**: `jdbc:h2:./data/easykanban`
- **Username**: `sa`
- **Password**: (empty)

**Note**: The `data/` directory is ignored by Git for security reasons.

## Troubleshooting

### Common Issues

1. **"Java not found" error**
   - Ensure JDK is installed and JAVA_HOME is set
   - Add Java to your system PATH

2. **"Maven not found" error**
   - Ensure Maven is installed and added to system PATH
   - Verify with `mvn -version`

3. **Compilation errors**
   - Run `mvn clean` to clear previous builds
   - Ensure you're using JDK 11 or higher

4. **Test failures**
   - Check if database files are locked by another process
   - Delete the `data/` directory and run tests again

5. **Permission errors**
   - Ensure you have write permissions in the project directory
   - On Unix systems, you might need to run `chmod +x` on scripts

### Getting Help

If you encounter issues:
1. Check the console output for error messages
2. Verify all prerequisites are correctly installed
3. Ensure you're in the correct project directory
4. Try running `mvn clean install` to refresh dependencies

## Dependencies
- **BCrypt**: Password hashing and verification
- **H2 Database**: Embedded database for data persistence
- **JUnit Jupiter**: Testing framework
- **Maven**: Dependency management and build tool

