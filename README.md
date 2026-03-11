# Domain API

> A RESTful microservice for domain and domain-option management in the TalentOS ecosystem

## Overview

**Domain API** is a Spring Boot-based microservice that centralizes reference-domain data used across TalentOS services (for example: programming languages, cloud providers, job categories, and other configurable option sets).

It provides a clean, scalable API to create, retrieve, update, delete, and search domains with pagination and filtering support. The service integrates seamlessly with other microservices through shared DTOs and response contracts from the common TalentOS library.

## Key Features

- **Domain CRUD**: Create, retrieve, update, and delete domain records
- **Domain Option Management**: Maintain option values associated with each domain
- **Advanced Search & Filtering**: Filter by domain name and option value with pagination support
- **Partial Option Retrieval**: Request only selected domain options by IDs
- **Transactional Update Logic**: Full replacement with option synchronization (add/remove)
- **Data Mapping**: MapStruct-based entity/DTO mapping for clean API contracts
- **Input Validation**: Jakarta Bean Validation for request safety and consistency
- **Global Error Handling**: Centralized exception management with standardized response envelope
- **API Documentation**: Interactive Swagger UI and OpenAPI 3 documentation
- **Multi-Environment Configuration**: Dedicated dev/test/prod profile properties

## Technology Stack

| Component                 | Technology                  | Version |
| ------------------------- | --------------------------- | ------- |
| **Framework**             | Spring Boot                 | 3.5.7   |
| **Language**              | Java                        | 25      |
| **Database**              | MySQL                       | 8.0+    |
| **ORM**                   | Spring Data JPA / Hibernate | 3.5.7   |
| **Validation**            | Jakarta Bean Validation     | 3.5.7   |
| **Mapping**               | MapStruct                   | 1.6.3   |
| **Boilerplate Reduction** | Lombok                      | 1.18.42 |
| **API Documentation**     | SpringDoc OpenAPI           | 2.8.13  |
| **Build Tool**            | Maven                       | 4.0.0   |

## Project Structure

```text
src/
├── main/
│   ├── java/com/certimetergroup/talentos/domainapi/
│   │   ├── DomainApiApplication.java          # Spring Boot entry point
│   │   ├── config/
│   │   │   └── SwaggerConfig.java             # OpenAPI/Swagger configuration
│   │   ├── controller/
│   │   │   ├── DomainController.java          # Domain and DomainOption endpoints
│   │   │   └── ExceptionController.java       # Global exception handling
│   │   ├── service/
│   │   │   ├── DomainService.java             # Domain business logic
│   │   │   └── DomainOptionService.java       # DomainOption business logic
│   │   ├── repository/
│   │   │   ├── DomainRepository.java          # Domain JPA repository
│   │   │   ├── DomainOptionRepository.java    # DomainOption JPA repository
│   │   │   └── specification/
│   │   │       └── DomainSpecification.java   # Dynamic query specifications
│   │   ├── mapper/
│   │   │   ├── DomainMapper.java              # Domain entity ↔ DTO mapping
│   │   │   └── DomainOptionMapper.java        # DomainOption mapping and sync logic
│   │   └── model/
│   │       ├── Domain.java                    # Domain entity
│   │       └── DomainOption.java              # DomainOption entity
│   └── resources/
│       ├── application.properties             # Shared/base properties
│       ├── application-dev.properties         # Development profile
│       ├── application-prod.properties        # Production profile
│       └── application-test.properties        # Test profile
└── test/
    └── java/com/certimetergroup/talentos/domainapi/
        └── DomainApiApplicationTests.java     # Spring context test
```

## Getting Started

### Prerequisites

- **Java 25** or higher
- **Maven 3.6+** (or Maven Wrapper)
- **MySQL 8.0+** (configured separately)
- Access to the internal artifact repository for `com.certimetergroup.talentos:commons`

### Installation

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd domain-api
   ```

2. **Set up environment variables**

   ```bash
   export SPRING_PROFILES_ACTIVE=dev
   export PORT=8082
   export SQL_DB_URL_DEV="jdbc:mysql://localhost:3306/easy_cv_dev"
   export SQL_DB_USER="root"
   export SQL_DB_PSW="password"
   ```

3. **Build the project**

   ```bash
   ./mvnw clean package
   ```

4. **Run the application**

   ```bash
   ./mvnw spring-boot:run
   ```

   Or run the built JAR:

   ```bash
   java -jar target/domainapi-0.0.1-SNAPSHOT.jar
   ```

The service starts on the configured `PORT` (for example, `8082`).

## API Usage

### Base URL

```text
http://localhost:8082/api/domains
```

### Response Contract

All endpoints return a standardized envelope:

- `responseEnum`: service outcome/status code semantic
- `payload`: endpoint-specific data (or null)

### Example Endpoints

**Get All Domains (with pagination and filtering)**

```http
GET /api/domains?page=1&pageSize=10&domainName=cloud&domainOptionValue=azure
```

**Get Domain by ID**

```http
GET /api/domains/{domainId}
```

**Get Domain by ID with specific options only**

```http
GET /api/domains/{domainId}?domainOptionIds=18,19
```

**Get Domain Option by ID**

```http
GET /api/domains/domain-option/{domainOptionId}
```

**Create Domain**

```http
POST /api/domains
```

**Replace Domain Data**

```http
PUT /api/domains/{domainId}
```

**Delete Domain**

```http
DELETE /api/domains/{domainId}
```

### API Documentation

Interactive API documentation is available at:

- **Swagger UI**: `http://localhost:8082/domain-api/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8082/domain-api/docs`

## Configuration

### Environment Profiles

**Development** (`application-dev.properties`):

- Development database URL via `SQL_DB_URL_DEV`
- Default logging level set to INFO

**Production** (`application-prod.properties`):

- Production database URL via `SQL_DB_URL_PROD`
- Environment-based credentials and settings

**Test** (`application-test.properties`):

- Test database URL via `SQL_DB_URL_TEST`

### Key Configuration Properties

```properties
# Application
spring.application.name=domain-api
server.port=${PORT}
spring.profiles.active=${SPRING_PROFILES_ACTIVE}

# Database
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# Externalized datasource properties (profile-specific files)
# application-dev.properties  -> ${SQL_DB_URL_DEV}
# application-test.properties -> ${SQL_DB_URL_TEST}
# application-prod.properties -> ${SQL_DB_URL_PROD}
spring.datasource.username=${SQL_DB_USER}
spring.datasource.password=${SQL_DB_PSW}

# Swagger/API Documentation
springdoc.api-docs.path=/domain-api/docs
springdoc.swagger-ui.path=/domain-api/swagger-ui.html
```

## Data Model

### Domain Entity

- **id**: Auto-generated primary key
- **name**: Required domain name
- **domainOptions**: Set of associated `DomainOption` records

### DomainOption Entity

- **id**: Auto-generated primary key
- **domainName**: Required many-to-one relation to `Domain`
- **value**: Required option value

## Error Handling

Global exception handling is provided via `ExceptionController` (`@RestControllerAdvice`).

Handled categories include:

- Validation errors (`ConstraintViolationException`, `MethodArgumentNotValidException`, `BindException`)
- Business exceptions (`FailureException`)
- Database/JDBC errors (`JDBCException`, `JDBCConnectionException`)
- Generic fallback (`Exception`)

Validation failures return `BAD_REQUEST` with a payload map of `field/path -> message`.

## Testing

Run unit/integration tests:

```bash
./mvnw test
```

The repository includes a Postman collection:

- `domain-api-talentos-postman-collection.json`

It provides:

- endpoint requests
- JSON schema validations
- workflow scenario (create -> update -> delete)

## Project Statistics

- **Language**: Java 25
- **Framework**: Spring Boot 3.5.7
- **Build System**: Maven
- **Database**: MySQL with Hibernate/JPA
- **API Documentation**: OpenAPI 3.0 / Swagger UI

## Dependencies Management

Dependencies are managed via Maven in `pom.xml`.

Core dependencies include:

- Spring Boot starters: Web, Data JPA, Validation, Test
- MySQL Connector/J
- MapStruct + annotation processor
- Lombok + Lombok-MapStruct binding
- SpringDoc OpenAPI
- Shared TalentOS commons module

Check for dependency updates with:

```bash
./mvnw dependency:resolve
./mvnw versions:display-dependency-updates
```

## Development Guidelines

### Code Structure

- **Controllers**: Handle HTTP requests and response contracts
- **Services**: Implement business rules and orchestration
- **Repositories**: Perform persistence operations and specifications
- **Mappers**: Convert entities to DTOs and synchronize option sets
- **Models**: Keep persistence concerns isolated from API payloads

### Best Practices

- Keep business logic in services, not controllers
- Validate request parameters and payloads at the boundary
- Use specifications for searchable, composable query filters
- Preserve DTO-based contracts to avoid exposing entity internals
- Maintain profile-based environment isolation for deployment safety

## Contributing

When contributing to this project:

1. Follow the existing package structure and naming conventions
2. Add validation and error handling for new request paths
3. Keep mapper logic deterministic and covered by tests when expanded
4. Update this README when introducing new endpoints or config changes

## License

MIT License. See [LICENSE](LICENSE) for details.

---

**Developed by Giuseppe Falcone**
