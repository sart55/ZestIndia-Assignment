# Student Management System

Spring Boot REST API for managing students with JWT authentication.

## Features

- CRUD operations for students
- JWT authentication
- Input validation
- Swagger API documentation
- Unit tests
- SQL Server database

## Tech Stack

- Java 17
- Spring Boot 4.0.5
- Spring Security & JWT
- SQL Server
- Maven

## Setup

### 1. Database

Create database:

```sql
CREATE DATABASE StudentManagementDB;
GO

USE StudentManagementDB;
GO

CREATE TABLE students (
    id BIGINT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    age INT NOT NULL,
    course NVARCHAR(100) NOT NULL,
    created_date DATETIME NOT NULL DEFAULT GETDATE()
);
GO
```

### 2. Run Application

```bash
# Update database password in src/main/resources/application.properties

# Build and run
mvn spring-boot:run
```

App runs at: http://localhost:8080

## API Endpoints

### Auth
- `POST /api/auth/login?username=<name>` - Get JWT token

### Students
- `GET /api/students` - Get all students
- `GET /api/students/{id}` - Get student by ID
- `POST /api/students` - Create student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

## API Documentation

Swagger UI: http://localhost:8080/api/swagger-ui.html

## Testing

```bash
mvn test
```

## Project Structure

```
src/main/java/com/example/zestindiaassignment/
├── controller/          - REST endpoints
├── service/             - Business logic
├── repository/          - Database queries
├── entity/              - JPA entities
├── dto/                 - Request/Response objects
├── exception/           - Exception handling
├── security/            - JWT configuration
└── config/              - Spring configuration
```

## Database Schema

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, IDENTITY |
| name | NVARCHAR(100) | NOT NULL |
| email | NVARCHAR(100) | NOT NULL, UNIQUE |
| age | INT | NOT NULL |
| course | NVARCHAR(100) | NOT NULL |
| created_date | DATETIME | NOT NULL, DEFAULT GETDATE() |
