# Professional Skills Testing System

A client-server application for professional skills testing built with Java, JavaFX, and PostgreSQL.

## Architecture

- **Server**: Multi-threaded Java server with business logic
- **Client**: JavaFX desktop application
- **Database**: PostgreSQL with JDBC access

## Project Structure

```
system-testing-professional-skills/
├── database/          # SQL scripts for database setup
├── common/            # Shared DTOs, enums, and utilities
├── server/            # Server application
└── client/            # JavaFX client application
```

## Requirements

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 16+

## Installation

### 1. Database Setup

```sql
-- Create database
CREATE DATABASE testingsystem;

-- Run scripts in order
\i database/create_tables.sql
\i database/insert_test_data.sql
```

### 2. Configure Server

Edit `server/src/main/resources/server.properties`:

```properties
server.port=12345
server.maxThreads=10
db.url=jdbc:postgresql://localhost:5432/testingsystem
db.user=postgres
db.password=your_password
db.poolSize=5
```

### 3. Build the Project

```bash
mvn clean package
```

## Running the Application

### Start the Server

```bash
# With database initialization
java -jar server/target/server-1.0-SNAPSHOT.jar --init

# Without initialization (normal start)
java -jar server/target/server-1.0-SNAPSHOT.jar
```

Or use Maven:
```bash
cd server
mvn javafx:run
```

### Start the Client

```bash
cd client
mvn javafx:run
```

Or:
```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar client/target/client-1.0-SNAPSHOT.jar
```

## Default Credentials

| Login | Password | Role |
|-------|----------|------|
| admin | password123 | Administrator |
| specialist1 | password123 | Specialist |
| specialist2 | password123 | Specialist |

## Features

### For Specialists
- View available tests
- Take tests with time tracking
- View assignments and deadlines
- View test results and history

### For Administrators
- User management (CRUD operations)
- Test management (create, edit, delete tests and questions)
- Assign tests to specialists
- View all results and statistics
- Generate reports

## Design Patterns Used

1. **Singleton**: Database connection pool, Server configuration, Session manager
2. **Factory Method**: DAO factory, Scene manager
3. **Template Method**: Question evaluation (SingleChoice vs MultipleChoice)
4. **Repository**: DAO layer for data access

## API Protocol

Client-server communication uses a simple text-based protocol over TCP/IP:

```
COMMAND|RESPONSE_CODE|DATA
```

### Commands
- `LOGIN` - User authentication
- `GET_TESTS` - Retrieve tests
- `START_TEST` - Begin test attempt
- `SUBMIT_TEST` - Submit answers
- `GET_RESULTS` - Retrieve results
- `MANAGE_USERS` - User administration
- `ASSIGN_TEST` - Test assignment
- `GET_STATISTICS` - View statistics

## Database Schema

The database consists of 6 tables in 3rd Normal Form:

1. **users** - User accounts (admins and specialists)
2. **tests** - Test definitions
3. **questions** - Test questions (identifying relationship with tests)
4. **answer_options** - Answer choices (identifying relationship with questions)
5. **test_assignments** - Test assignments to users
6. **test_results** - Test completion results

## Development

### Module Dependencies

- **common**: No dependencies
- **server**: Depends on common
- **client**: Depends on common

### Adding New Features

1. Add DTO to `common/src/main/java/com/testingsystem/common/dto/`
2. Add entity to `server/src/main/java/com/testingsystem/server/model/`
3. Add DAO interface and implementation
4. Add service layer methods
5. Add network protocol commands
6. Add client-side UI components

## Troubleshooting

### Server won't start
- Check PostgreSQL is running
- Verify database credentials in server.properties
- Ensure port 12345 is not in use

### Client can't connect
- Verify server is running
- Check firewall settings
- Confirm server host/port in ServerConnection.java

### Database errors
- Run create_tables.sql to verify schema
- Check PostgreSQL logs for details

## License

This project is created for educational purposes.

## Authors

Professional Skills Testing System - Coursework Project
