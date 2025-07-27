# Coding Guidelines

## Project Structure

Follow **package-by-feature/module** and in each module **package-by-layer** code organization style:

```shell
project-root/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mycompany/projectname/
│   │   │       ├── config/
│   │   │       ├── shared/
│   │   │       │     ├── models/
│   │   │       │     ├── exceptions/
│   │   │       │     ├── utils/
│   │   │       ├── module1/
│   │   │       │     ├── api/
│   │   │       │     │   ├── controllers/
│   │   │       │     │   └── dtos/
│   │   │       │     ├── config/
│   │   │       │     ├── core/
│   │   │       │     │   ├── entities/
│   │   │       │     │   ├── exceptions/
│   │   │       │     │   ├── mappers/
│   │   │       │     │   ├── models/
│   │   │       │     │   ├── repositories/
│   │   │       │     │   └── services/
│   │   │       │     ├── jobs/
│   │   │       │     ├── eventhandlers/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│   │   └── java/
│   │   │   └── com/mycompany/projectname/
│   │   │       ├── module1/
│   │   │       │     ├── api/
│   │   │       │     │   ├── controllers/
│   │   │       │     ├── core/
│   │   │       │     │   └── services/
└── README.md
```

## Java Guidelines

- Use Java 21 features where appropriate (records, text blocks, pattern matching, etc.)
- Follow standard Java naming conventions
- Use meaningful variable and method names
- Use `public` access modifier only when necessary

## Logging

- **Use a proper logging framework.**  
  Never use `System.out.println()` for application logging. Rely on SLF4J (or a compatible abstraction) and your chosen backend (Logback, Log4j2, etc.).

- **Protect sensitive data.**  
  Ensure that no credentials, personal information, or other confidential details ever appear in log output.

- **Guard expensive log calls.**  
  When building verbose messages at `DEBUG` or `TRACE` level, especially those involving method calls or complex string concatenations, wrap them in a level check or use suppliers:

    ```
    if (logger.isDebugEnabled()) {
        logger.debug("Detailed state: {}", computeExpensiveDetails());
    }
    ```
  
## Spring Boot Guidelines

### Prefer Constructor Injection over Field/Setter Injection
- Declare all the mandatory dependencies as `final` fields and inject them through the constructor.
- Spring will auto-detect if there is only one constructor, no need to add `@Autowired` on the constructor.
- Avoid field/setter injection in production code.

### Prefer package-private over public for Spring components
- Declare Controllers, their request-handling methods, `@Configuration` classes and `@Bean` methods with default (package-private) visibility whenever possible. There's no obligation to make everything `public`.

### Organize Configuration with Typed Properties
- Group application-specific configuration properties with a common prefix in `application.properties` or `.yml`.
- Bind them to `@ConfigurationProperties` classes with validation annotations so that the application will fail fast if the configuration is invalid.
- Prefer environment variables instead of profiles for passing different configuration properties for different environments.

### Define Clear Transaction Boundaries
- Define each Service-layer method as a transactional unit.
- Annotate query-only methods with `@Transactional(readOnly = true)`.
- Annotate data-modifying methods with `@Transactional`.
- Limit the code inside each transaction to the smallest necessary scope.

### Separate Web Layer from Persistence Layer
- Don't expose entities directly as responses in controllers.
- Define explicit request and response record (DTO) classes instead.
- Apply Jakarta Validation annotations on your request records to enforce input rules.

### Web application best practices
- Use pagination for displaying data that may contain an unbounded number of items.

### Use Command Objects for Business Operations
- Create purpose-built records (e.g., `CreateOrderCmd`) to wrap input data.
- Accept these models in your service methods to drive creation or update workflows.

### Centralize Exception Handling
- Define a global handler class annotated with `@RestControllerAdvice` for REST APIs using `@ExceptionHandler` methods to handle specific exceptions.
- Return consistent error responses.

### Actuator
- Expose only essential actuator endpoints (such as `/health`, `/info`, `/metrics`) without requiring authentication. All the other actuator endpoints must be secured.

### Internationalization with ResourceBundles
- Externalize all user-facing text such as labels, prompts, and messages into ResourceBundles rather than embedding them in code.

### Use Testcontainers for integration tests
- Spin up real services (databases, message brokers, etc.) in your integration tests to mirror production environments.

### Use random port for integration tests
- When writing integration tests, start the application on a random available port to avoid port conflicts by annotating the test class with:

    ```java
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    class MyControllerTests {
    }
    ```

## Spring Data JPA Guidelines
- While using Spring Data JPA, disable the Open Session in View filter by setting ` spring.jpa.open-in-view=false` in `application.properties/yml.`
- Create custom Spring Data JPA methods with meaningful method names using JPQL queries instead of using long derived query method names.
- Load the necessary relations using `join fetch` with JPQL to avoid N + 1 Select problem.

## Database Schema Management Guidelines
Use Flyway for database migrations:

- Migration scripts should be in `src/main/resources/db/migration`
- Naming convention: `V{version}__{description}.sql`
- JPA/Hibernate is configured with `ddl-auto=validate` to ensure schema matches entities

## Testing Guidelines
- **Unit Tests**: Test individual components in isolation using mocks if required
- **Integration Tests**: Test interactions between components using Testcontainers
- **Use descriptive test names** that explain what the test is verifying
- **Follow the Given-When-Then pattern** for a clear test structure
- **Use AssertJ for assertions** for more readable assertions
- **Prefer testing with real dependencies** in unit tests as much as possible instead of using mocks
- **Use Testcontainers for integration tests** to test with real databases, message brokers, etc
- **TestcontainersConfiguration.java**: Configures database, message broker, etc containers for tests
- **BaseIT.java**: Base class for integration tests that sets up:
    - Spring Boot test context using a random port
    - MockMvcTester for HTTP requests
    - Import `TestcontainersConfiguration.java`
- **Min 80% Code Coverage**: Aim for good code coverage, but be pragmatic. Don't write useless tests just for the sake of code coverage metrics.

