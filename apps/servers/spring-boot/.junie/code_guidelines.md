# Code Guidelines and Best Practices

This document outlines the coding standards and best practices for the Java 25 + Spring Boot 4 project to ensure consistency and maintain high code quality across the codebase.

## General Guidelines

### Naming Conventions
* **Packages:** Use lowercase, dot-separated names (e.g., `com.julian.razif.kanban.service`).
* **Classes and Interfaces:** Use PascalCase (e.g., `OrderService`, `TaskRepository`).
* **Methods and Variables:** Use camelCase (e.g., `calculateTotal()`, `isactive`).
* **Constants:** Use SCREAMING_SNAKE_CASE (e.g., `MAX_RETRY_ATTEMPTS`).
* **JSON Properties:** Consistently use `camelCase` or `snake_case` as per project preference (default to `camelCase` for compatibility with Java).

### Code Organization
* Organize code by feature/domain rather than by technical layer where possible but maintain a clear separation of concerns.
* **Internationalization:** Externalize all user-facing text (labels, prompts, messages) into `ResourceBundles`. Avoid hardcoding strings in the code.

## Java Code Style

### Classes and Interfaces
* **Visibility:** Prefer package-private over `public` for Spring components (Controllers, Services, `@Configuration` classes, `@Bean` methods). This reinforces encapsulation.
* **Immutability:** Use `record` for DTOs, Commands, and Queries to ensure immutability and reduce boilerplate.
* Use `final` for fields that are not intended to be changed after initialization.

### Methods
* Keep methods short and focused on a single responsibility.
* Avoid long parameter lists; use objects or records to group related parameters.

### Exception Handling
* **Centralized Handling:** Use a global handler class annotated with `@ControllerAdvice` (or `@RestControllerAdvice`) with `@ExceptionHandler` methods.
* Return consistent error responses, preferably following the ProblemDetails format ([RFC 9457](https://www.rfc-editor.org/rfc/rfc9457)).
* Avoid catching generic `Exception` unless necessary for logging at the top level.

### Null Safety
* Use `Optional<T>` for return types where a value might be missing. Avoid using `Optional` for parameters or fields.
* Leverage Java's `Objects.requireNonNull()` for early validation of mandatory arguments.
* Use Jakarta Validation annotations (`@NotNull`, `@NotEmpty`, etc.) on request DTOs.

## JPA and Database

### Entity Classes
* **Separation:** Don't expose entities directly as API responses. Use DTOs instead.
* **OSIV:** Disable Open Session in View by setting `spring.jpa.open-in-view=false`.
* Fetch associations explicitly via fetch joins, entity graphs, or dedicated queries to avoid N+1 problems.
* Use appropriate JPA annotations and prefer `GenerationType.SEQUENCE` or `IDENTITY` for IDs depending on the database.

### Repository Interfaces
* Use Spring Data JPA repositories.
* Keep repository methods focused. Complex queries should be moved to custom repository implementations or handled via Specifications/QueryDSL.

## Spring Framework

### Dependency Injection
* **Constructor Injection:** Prefer constructor injection over field or setter injection. 
* Declare dependencies as `final` fields. 
* Avoid `@Autowired` on constructors; Spring detects it automatically if there's only one constructor.

### Transaction Management
* **Boundaries:** Define each Service-layer method as a transactional unit.
* Use `@Transactional(readOnly = true)` for query-only methods.
* Use `@Transactional` for data-modifying methods.
* Keep the code inside transactions to the smallest necessary scope.

### REST Controllers
* **URL Design:** Use versioned, resource-oriented URLs (e.g., `/api/v1/tasks`).
* **Request/Response:** Use explicit request and response records (DTOs). Use `ResponseEntity<T>` to return correct HTTP status codes.
* **Commands:** Use purpose-built command records (e.g., `CreateTaskCommand`) to wrap input data for business operations.
* **Pagination:** Use pagination for collection resources that may contain many items.
* **JSON Payload:** Top-level data structure should always be a JSON object.

### Configuration
* **Typed Properties:** Group application-specific properties with a common prefix and bind them to `@ConfigurationProperties` classes with validation.
* Prefer environment variables over profiles for environment-specific configurations.

## Testing
* **Testcontainers:** Use real services (databases, brokers) in integration tests via Testcontainers. Use specific versions for images, not `latest`.
* **Random Port:** Run integration tests on a random available port using `@SpringBootTest(webEnvironment = RANDOM_PORT)`.
* Aim for high test coverage, including unit tests for business logic and integration tests for repository and controller layers.

## Logging
* **Framework:** Use SLF4J with Logback (default in Spring Boot). Never use `System.out.println()`.
* **Security:** Never log sensitive data (credentials, PII).
* **Performance:** Guard expensive log calls with level checks or use the Fluent API with Suppliers.
  ```
  logger.atDebug()
        .setMessage("State: {}")
        .addArgument(() -> expensiveOperation())
        .log();
  ```

## Security
* **Actuator:** Expose only essential endpoints (`/health`, `/info`, `/metrics`) without authentication. Secure all others.
* Use Spring Security for authentication and authorization.
* Use JWT (JJWT) for stateless authentication where appropriate.

## Performance
* Minimize the scope of transactions to reduce database lock duration.
* Use efficient data fetching strategies in JPA.
* Avoid unnecessary object creation in hot loops.

## Caching

### When to Use Caching
* Use caching for data that is expensive to compute or fetch and changes infrequently.
* Don't cache data that is highly dynamic or has strict real-time requirements.

### Caching Annotations
* Use Spring's `@Cacheable`, `@CachePut`, and `@CacheEvict` annotations.
* Ensure a proper CacheManager is configured (e.g., Caffeine, Redis).

### Cache Invalidation
* Define clear invalidation strategies (TTL or explicit evict on update/delete).
* Be mindful of cache consistency in distributed environments.
