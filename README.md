# EasyKanban - Task Management System

A Java-based Kanban task management application with secure user authentication and database persistence.

## Features

### Security
- **Password Hashing**: Secure password storage using BCrypt hashing algorithm
- **User Authentication**: Database-backed login system with encrypted credentials
- **Session Management**: Secure user session handling

### Database Integration
- **H2 Database**: Embedded database for data persistence
- **User Management**: Complete CRUD operations for user accounts
- **Task Management**: Full task lifecycle management with database storage
- **Data Analytics**: Task duration analysis and reporting

### Task Management
- Create, read, update, and delete tasks
- Search tasks by name, developer, or status
- Track task duration and calculate total hours
- Filter tasks by completion status
- User-specific task isolation

## Dependencies
- BCrypt for password hashing
- H2 Database for data persistence
- JUnit for testing
- Maven for dependency management

## Testing
Refer to the file named: "References&Rubric_EbenNkuluMwema_ST10091324_PoE" in the project files for code references.

Note: GitHub Actions may have package resolution issues, but all tests pass locally in the IDE.
