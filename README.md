# User Management Application

## Overview
This is a simple User Management application built using Spring Boot. It exposes a RESTful API that allows you to:
- Retrieve a list of users or a specific user by ID.
- Create new users.
- Update existing users.
- Delete users.

User authentication is handled using Spring Security with Basic Authentication, ensuring that only authenticated users can update or delete their own profiles.

## Features
- **CRUD Operations:** Create, read, update, and delete users.
- **User Authentication:** Basic authentication to secure endpoints.
- **Validation:** Input data validation using `javax.validation` annotations.
- **Global Exception Handling:** Consistent error responses via a centralized exception handler.
- **API Documentation:** Swagger/OpenAPI integration for interactive API documentation.
- **Docker Support:** Dockerfile provided for containerized deployment.
- **CI/CD Pipeline:** GitHub Actions workflow for building, and containerizing the application.

## Technologies Used
- Java 21
- Spring Boot
- Spring Security
- Hibernate
- PostgreSQL
- Maven
- Docker
- GitHub Actions

## Prerequisites
Before running this project, ensure you have the following installed:
- JDK 21
- Maven 
- PostgreSQL

## Database Setup

### 1. Install PostgreSQL
Download and install PostgreSQL from the [official website](https://www.postgresql.org/download/).

### 2. Create the Database
Connect to your PostgreSQL instance (using psql, pgAdmin, etc.) and create a new database named `usermanagement`:
```sql
CREATE DATABASE usermanagement;
```

### 3. Configure the Database Credentials

Ensure that your application.properties file is configured with the correct database credentials:

- spring.datasource.url=jdbc:postgresql://localhost:port/usermanagement
- spring.datasource.username=username
- spring.datasource.password=password

### 4. (Optional) Create a Custom Schema

If you prefer using a custom schema instead of the default public:

- Create the schema in PostgreSQL:
```sql
CREATE SCHEMA my_schema;
```
- Update your application.properties to specify the schema:
```properties
spring.jpa.properties.hibernate.default_schema=my_schema
```

## Running the Application
### Using Maven

- Clone the Repository:
```bash
git clone <repository_url>
cd <repository_directory>
```

- Build the Project:
```bash
- mvn clean package
```
- Run the Application:
```bash
mvn spring-boot:run
```

- Alternatively, run the packaged JAR file:
```bash
java -jar target/usermanagement-0.0.1-SNAPSHOT.jar
```

## CI/CD Pipeline
This project includes a GitHub Actions workflod(build-c-.yml) that:
- Sets up JDK21
- Builds the project using Maven
- Packages the application as a JAR file
- Builds a Docker image

## API Documentation
After starting the application, you can access the Swagger UI for API documentation at:
```bash
http://localhost:8080/swagger-ui.html
```
## Future Enhancements
- Implement JWT-based authentication
- Add support for user roles and permissions
- Integrate database migrations using Flyway or Liquibase
- Improve logging and monitoring for production use
## License
This project is open-source and available under the [MIT License](LICENSE).

## Contact
Feel free to reach out if you have any questions or feedback