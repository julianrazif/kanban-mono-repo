# Kanban - Development Guidelines

This document outlines the architectural principles, coding standards, and best practices for the Kanban project.

## 1. Architecture Guidelines

### Overview
The Kanban application is a RESTful web service written in Java using Spring Boot. It follows a hexagonal (ports and adapters) architecture organized into three layers: **domain**, **application**, and **infrastructure**.

### Architectural Principles
1. **Separation of Concerns**: Separate business logic from technical implementations.
2. **Dependency Inversion**: Core business logic depends on abstractions, not concrete implementations.
3. **Domain-Driven Design**: Organize around business domains and capabilities.
4. **Resilience**: Implement patterns like circuit breaker, retry, and caching for reliability.
5. **Observability**: Ensure logging, metrics, and tracing are available across layers.

### Layers
- **Domain Layer**: Entities, value objects, domain services, and outgoing ports. Pure business logic with no framework dependencies.
- **Application Layer**: Use cases, incoming ports, and DTOs. Coordinates domain operations and mappings.
- **Infrastructure Layer**: Driving (REST) and driven (Persistence, API clients) adapters. Handles framework-specific details.

### Data Flow
1. **Request**: Driving adapter receives input.
2. **Application**: Maps input to Command/Query and invokes use case.
3. **Domain**: Executes business rules, calling outgoing ports for persistence or external systems.
4. **Infrastructure**: Driven adapters fulfill port calls.
5. **Response**: Application maps results to Response DTOs; driving adapter returns them.

### Project Structure
```text
src/main/java/com/julian/razif/kanban/
├── <domain-name>/                   # Feature/Domain-based packaging
│   ├── domain/                      # model, service, event, port
│   ├── application/                 # service, port, dto
│   └── infrastructure/              # web, persistence, messaging, config, mapping
└── common/                          # exception, security, util
```

## 2. Coding Standards

### 2.1 Naming Conventions
- **Packages**: lowercase, dot-separated (`com.julian.razif.kanban`).
- **Classes/Interfaces**: PascalCase (`OrderService`).
- **Methods/Variables**: camelCase (`calculateTotal`).
- **Constants**: SCREAMING_SNAKE_CASE (`MAX_RETRY_ATTEMPTS`).
- **JSON Properties**: camelCase.

### 2.2 Java Code Style
- **Immutability**: Use `record` for DTOs, Commands, and Queries. Use `final` for non-modifiable fields.
- **Visibility**: Prefer package-private for Spring components (Controllers, Services, Configurations) to reinforce encapsulation.
- **Null Safety**: Use `Optional<T>` for return types. Avoid `Optional` for parameters. Use `Objects.requireNonNull()` or Jakarta Validation (`@NotNull`) for early validation.
- **Methods**: Keep methods short and focused on a single responsibility.

## 3. Spring Boot Best Practices

### 3.1 Dependency Injection
- **Constructor Injection**: Always prefer constructor injection over field or setter injection. This ensures objects are always in a valid state and facilitates testing.
- **Mandatory Dependencies**: Declare as `final` fields. Spring automatically detects single constructors, so `@Autowired` is unnecessary.
- **Builders**: Use constructor injection to customize Spring-provided builders.

```java
@Service
class OrderService {
    private final OrderRepository orderRepository;
    private final RestClient restClient;

    public OrderService(OrderRepository orderRepository, RestClient.Builder builder) {
        this.orderRepository = orderRepository;
        this.restClient = builder
                .baseUrl("http://catalog-service.com")
                .build();
    }
}
```

### 3.2 Configuration Management
- **Typed Properties**: Group properties with a common prefix and bind them to `@ConfigurationProperties` classes with validation.
- **Environment Variables**: Prefer environment variables over profiles for environment-specific settings.

### 3.3 Exception Handling
- **Centralized Handling**: Use `@RestControllerAdvice` with `@ExceptionHandler` methods to catch specific exceptions globally.
- **Consistent Responses**: Follow the ProblemDetails format ([RFC 9457](https://www.rfc-editor.org/rfc/rfc9457)).
- **Avoid Generic Catch**: Do not catch generic `Exception` unless necessary for top-level logging.

### 3.4 Internationalization
- **ResourceBundles**: Externalize all user-facing text into locale-specific ResourceBundle files instead of hardcoding strings.

## 4. REST API Design

### 4.1 URL and Resource Design
- **Versioned URLs**: Structure endpoints as `/api/v{version}/resources` (e.g., `/api/v1/tasks`).
- **Consistency**: Use uniform patterns for collections (`/tasks`) and sub-resources (`/tasks/{id}/comments`).
- **Pagination**: Implement pagination for unbounded collection resources.
- **Zalando Guidelines**: Refer to [Zalando RESTful API Guidelines](https://opensource.zalando.com/restful-api-guidelines/) for comprehensive standards.

### 4.2 Request and Response Handling
- **Explicit DTOs**: Never expose entities directly as API responses. Define dedicated request and response records.
- **Status Codes**: Use `ResponseEntity<T>` to return appropriate HTTP status codes (200 OK, 201 Created, 404 Not Found).
- **Validation**: Apply Jakarta Validation annotations on request DTOs.
- **JSON Payload**: Always use a JSON object as the top-level structure.

### 4.3 Business Operations
- **Command Objects**: Use purpose-built records (e.g., `CreateTaskCommand`) to wrap input data for business operations, clearly communicating required fields.

## 5. Persistence and JPA

### 5.1 Transaction Management
- **Boundaries**: Define Service-layer methods as transactional units.
- **Optimizations**: Use `@Transactional(readOnly = true)` for queries and `@Transactional` for modifications.
- **Scope**: Keep transactions as brief as possible to minimize lock contention and connection hold time.

### 5.2 Entity Design and Fetching
- **OSIV**: Disable Open Session in View (`spring.jpa.open-in-view=false`) to prevent N+1 select problems and force explicit fetching.
- **Explicit Fetching**: Fetch associations using fetch joins or entity graphs to avoid `LazyInitializationException`.
- **Repositories**: Use Spring Data JPA repositories. Move complex queries to custom implementations or use Specifications/QueryDSL for dynamic filtering.

## 6. Observability and Security

### 6.1 Logging
- **Framework**: Use SLF4J with Logback. Never use `System.out.println()`.
- **Sensitive Data**: Ensure credentials and PII are never logged.
- **Performance**: Guard expensive log calls or use the Fluent API with Suppliers to avoid unnecessary string concatenation.

```
logger.atDebug()
    .setMessage("State: {}")
    .addArgument(() -> computeExpensiveDetails())
    .log();
```

### 6.2 Security and Actuator
- **Actuator**: Expose only essential endpoints (`/health`, `/info`, `/metrics`) anonymously. Secure all others.
- **Authentication**: Use Spring Security with JWT for stateless authentication.

## 7. Testing Strategy
- **Testcontainers**: Use real services (databases, brokers) in integration tests via Testcontainers with specific version tags.
- **Random Port**: Avoid port conflicts by using a random available port.

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {
}
```

- **Layered Testing**: 
  - **Domain**: Pure unit tests (no Spring).
  - **Application**: Use-case tests with fakes/stubs for ports.
  - **Infrastructure**: Integration tests for adapters.

## 8. Caching and Performance
- **Caching**: Use `@Cacheable` for expensive, infrequently changing data. Configure a proper `CacheManager` (e.g., Caffeine, Redis).
- **Invalidation**: Define clear TTL or explicit eviction strategies.
- **Performance**: Minimize transaction scope and avoid unnecessary object creation in hot loops.
