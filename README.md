# Dashboard API

## Overview
This is a Spring Boot application for the Dashboard API.

## Requirements
- Java 21
- Maven (wrapper included)
- PostgreSQL

## Configuration
The application relies on the following environment variables for database connection:
- `DB_URL`: The JDBC URL for the database (e.g., `jdbc:postgresql://localhost:5432/dashboard`)
- `DB_USERNAME`: The database username
- `DB_PASSWORD`: The database password

## Build and Run
To build the project:
```bash
./mvnw clean package
```

To run the application:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/dashboard
export DB_USERNAME=postgres
export DB_PASSWORD=password
./mvnw spring-boot:run
```

## Profiles
- `dev`: Active by default.
- `prod`: Use by setting `spring.profiles.active=prod`.

## Testing
To run tests, ensure you have the database environment variables set or configure a test profile.
```bash
./mvnw test
```
