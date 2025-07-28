# Spring Boot Blog REST API

This is a Spring Boot application that provides a REST API for a simple blog. 
It includes features for user authentication, post management, and comments.

## Tech Stack

*   **Backend:** Java, Spring Boot, Spring Modulith, Spring Data JPA, Spring Security
*   **Database:** PostgreSQL, Flyway
*   **Authentication:** Spring Security JWT
*   **Build Tool:** Maven
*   **Testing:** JUnit 5, Testcontainers
*   **API Documentation:** OpenAPI (Swagger)

## Features

*   **User Authentication:**
    *   User registration
    *   User login with JWT token
*   **Post Management:**
    *   Create a new post
    *   Get a list of all posts
    *   Get a single post by slug
    *   Update a post
*   **Comment Management:**
    *   Add a comment to a post
    *   Get all comments for a post

## Key Architecture Decisions

*   **Modular Architecture:** The application is divided into modules (`content`, `users`) to promote separation of concerns and maintainability. Spring Modulith is used to enforce module boundaries.
*   **RESTful API:** The application exposes a RESTful API for clients to interact with.
*   **JWT-based Authentication:** Authentication is handled using JSON Web Tokens (JWTs), which are issued upon successful login and are required for accessing protected endpoints.
*   **JPA and Hibernate:** The application uses Spring Data JPA and Hibernate for data persistence, which simplifies database interactions.
*   **Flyway for Database Migrations:** Database schema changes are managed using Flyway, which allows for version-controlled database migrations.
*   **Testcontainers for Integration Testing:** Integration tests are run against a real PostgreSQL database managed by Testcontainers, ensuring that the tests are run in an environment that is as close to production as possible.

## How to Test and Run the Application

### Prerequisites

*   Java 24 or higher
*   Docker and Docker Compose

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/sivaprasadreddy/spring-boot-demo.git
    cd spring-boot-demo
    ```
2.  **Run the application using Docker Compose:**
    ```bash
    docker-compose up -d
    ```
    The application will be running at `http://localhost:8080`.

### Running the Tests

1.  **Run the tests using Maven:**
    ```bash
    ./mvnw test
    ```

This will run all the unit and integration tests for the application.
