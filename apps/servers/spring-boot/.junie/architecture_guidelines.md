# Architecture Guidelines and Best Practices

## Overview
The Kanban Board application is a RESTful web service written in Java using Spring Boot. It follows a hexagonal (ports and adapters) architecture organized into three layers: **domain**, **application**, and **infrastructure**. The goal is clear separation of concerns, easy testing, and replaceable adapters.

## Architectural Principles

1. **Separation of Concerns**: The system separates business logic from technical implementations.
2. **Dependency Inversion**: Core business logic depends on abstractions, not concrete implementations.
3. **Domain-Driven Design**: The system is organized around business domains and capabilities.
4. **Resilience**: The system implements patterns like circuit breaker, retry, and caching for reliability.
5. **Observability**: Ensure logging, metrics, and tracing are available across layers.

## Layers

### Domain Layer (core business)
- **What lives here**: Entities/aggregates, value objects, domain services, domain events, validation rules. Outgoing ports (interfaces) for persistence, messaging, and external systems.
- **Rules**: No Spring or framework annotations. No knowledge of transport, database, or vendors. Pure business model and policies.
- **Benefits**: Highly testable, stable API for the rest of the system.

### Application Layer (use cases)
- **What lives here**: Use cases (application services), incoming ports (interfaces) exposed to driving adapters, DTOs/commands/queries for use-case boundaries, orchestration of domain logic and ports.
- **Rules**: Minimal framework use; keep it thin. Coordinates domain operations, transactions, and mapping between DTOs and domain models. No infrastructure details.
- **Benefits**: Clear, intention-revealing workflows; easy to test with fakes for outgoing ports.

### Infrastructure Layer (adapters and technical concerns)
- **What lives here**: Driving adapters (REST/controllers, messaging consumers, CLI), driven adapters (persistence implementations, HTTP/gRPC clients, messaging producers/consumers), mappers, Spring configuration and wiring, security setup, resilience policies, logging/observability setup.
- **Rules**: Implements ports defined in domain/application. Handles frameworks, protocols, and vendor SDKs. Domain/application should not leak transport or persistence details.
- **Benefits**: Swappable adapters (e.g., Stripe → Adyen, REST → gRPC) without touching domain/application.

## Data Flow
1. **Request in**: Driving adapter (e.g., REST controller) receives input.
2. **Application**: Maps input to a command/query DTO and invokes an incoming port/use case.
3. **Domain**: Use case executes business rules, invoking outgoing ports defined in the domain to reach persistence or external systems.
4. **Adapters (driven)**: Concrete implementations in infrastructure fulfill outgoing port calls (DB, external APIs, messaging).
5. **Response out**: Application maps domain results to response DTOs; driving adapter returns them to the caller.

## Resilience (applied in infrastructure, guided by domain needs)
- **Circuit Breaker**, **Retry**, **Caching**, **Fallback** around driven adapters (DB, external APIs, messaging). Surface failures as domain-meaningful errors.
- Keep policies configurable; avoid embedding vendor-specific logic in the domain/application.

## Security
- Apply authentication/authorization in infrastructure (filters/controllers), enforcing capabilities required by application use cases.
- Protect data in transit and at rest; add audit logging for sensitive actions.
- Keep domain/application free of security framework details; pass along caller context as needed via application DTOs or dedicated context objects.

## Testing Strategy
- **Domain**: Pure unit tests with no Spring; use fakes for ports.
- **Application**: Use-case tests with fakes/stubs for outgoing ports.
- **Infrastructure**: Integration tests for adapters (REST, persistence, external clients), plus contract tests where appropriate.

## Project Structure
The project follows a standard Maven layout, with the source code organized into three main layers: `domain`, `application`, and `infrastructure`.

```text
src/main/java/com/julian/razif/kanban/
├── <domain-name>/                   # Feature/Domain-based packaging
│   ├── domain/                      # Core business logic
│   │   ├── model/                   # Entities, Aggregates, Value Objects
│   │   ├── service/                 # Domain Services
│   │   ├── event/                   # Domain Events
│   │   └── port/                    # Outgoing ports (interfaces)
│   ├── application/                 # Use cases
│   │   ├── service/                 # Application Services
│   │   ├── port/                    # Incoming ports (interfaces)
│   │   └── dto/                     # Request/Response DTOs, Commands, Queries
│   └── infrastructure/              # Adapters and technical concerns
│       ├── web/                     # Controllers, REST adapters
│       ├── persistence/             # Repository implementations
│       ├── messaging/               # Message brokers consumers/producers
│       ├── external/                # External API clients
│       ├── config/                  # Spring configuration
│       └── mapping/                 # MapStruct or manual mappers
└── common/                          # Cross-cutting concerns
    ├── exception/                   # Global exception handling
    ├── security/                    # Security configuration and filters
    └── util/                        # Shared utility classes
```

