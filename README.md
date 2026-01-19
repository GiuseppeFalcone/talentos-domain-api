# Domain API (TalentOS)

A RESTful API for managing domain data in the Easy-CV/TalentOS project. This API provides endpoints for managing domains and domain options, such as schools, technologies, programming languages, soft skills, and degrees.

## Technology Stack

- **Java 25**
- **Spring Boot 3.5.7**
  - Spring Data JPA
  - Spring Web
  - Spring Validation
- **MySQL** - Relational database
- **Maven** - Build and dependency management
- **Lombok** - Reduce boilerplate code
- **MapStruct** - Object mapping
- **SpringDoc OpenAPI** - API documentation (Swagger)

## Features

- CRUD operations for domains and domain options
- Pagination support for listing domains
- Filtering by domain name and domain option values
- RESTful API design with standard HTTP methods
- Comprehensive API documentation via Swagger UI
- Input validation
- Centralized exception handling
- Multiple environment profiles (dev, test, prod)

## Prerequisites

Before running this application, ensure you have:

- **Java 25** or later installed
- **Maven 3.6+** installed
- **MySQL 8.0+** running
- A MySQL database created for the application

## Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/GiuseppeFalcone/talentos-domain-api.git
cd talentos-domain-api
```

### 2. Configure environment variables

Create a `.env` file based on the `.env.example` file in `src/main/resources/`:

```bash
cp src/main/resources/.env.example .env
```

Edit the `.env` file and configure the following variables:

```properties
SPRING_PROFILES_ACTIVE=dev

SQL_DB_URL_DEV=jdbc:mysql://localhost:3306/your_database_name
SQL_DB_URL_TEST=jdbc:mysql://localhost:3306/your_test_database_name
SQL_DB_URL_PROD=jdbc:mysql://localhost:3306/your_prod_database_name
SQL_DB_USER=your_username
SQL_DB_PSW=your_password

PASSWORD_PEPPER=a_secure_string
JWT_REFRESH_KEY=a_long_and_secure_string
```

### 3. Set up the database

Import the provided SQL dump to initialize your database with sample data:

```bash
mysql -u your_username -p your_database_name < dump-easy-cv-dev-domainapi-202512181235.sql
```

This will create the following tables:
- `domain` - Stores domain categories (e.g., school, technology, soft_skill)
- `domain_option` - Stores domain values (e.g., MIT, Harvard, Java, Python)

### 4. Build the project

Using Maven Wrapper (recommended):

```bash
./mvnw clean install
```

Or using Maven:

```bash
mvn clean install
```

## Running the Application

### Using Maven Wrapper

```bash
./mvnw spring-boot:run
```

### Using Maven

```bash
mvn spring-boot:run
```

### Using Java

```bash
java -jar target/domainapi-0.0.1-SNAPSHOT.jar
```

The application will start on the port specified in your configuration (check `application.properties`).

## API Documentation

Once the application is running, you can access the API documentation:

- **Swagger UI**: `http://localhost:{PORT}/domain-api/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:{PORT}/domain-api/docs`

Replace `{PORT}` with the port number configured in your environment.

### Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/domains` | Get all domains (with pagination and filtering) |
| GET | `/api/domains/{domainId}` | Get a specific domain by ID |
| GET | `/api/domains/domain-option/{domainOptionId}` | Get a specific domain option by ID |
| POST | `/api/domains` | Create a new domain |
| PUT | `/api/domains/{domainId}` | Update an existing domain |
| DELETE | `/api/domains/{domainId}` | Delete a domain |

### Query Parameters

- **page**: Page number (default: 1)
- **pageSize**: Number of items per page (default: 5)
- **domainName**: Filter by domain name
- **domainOptionValue**: Filter by domain option value
- **domainOptionIds**: Filter domain options by IDs (when getting a specific domain)

### Sample Request

Using cURL to get all domains:

```bash
curl -X GET "http://localhost:8082/api/domains?page=1&pageSize=5" -H "accept: application/json"
```

Using the provided Postman collection:

Import the `domain-api-talentos.json` file into Postman for pre-configured requests with validation tests.

## Project Structure

```
talentos-domain-api/
├── src/
│   ├── main/
│   │   ├── java/com/certimetergroup/talentos/domainapi/
│   │   │   ├── config/          # Configuration classes (Swagger, etc.)
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── mapper/          # MapStruct mappers for DTO conversions
│   │   │   ├── model/           # JPA entities
│   │   │   ├── repository/      # Spring Data JPA repositories
│   │   │   ├── service/         # Business logic layer
│   │   │   └── DomainApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-test.properties
│   │       ├── application-prod.properties
│   │       └── .env.example
│   └── test/
│       └── java/                # Test classes
├── pom.xml                      # Maven configuration
├── domain-api-talentos.json     # Postman collection
├── dump-easy-cv-dev-domainapi-202512181235.sql  # Database dump
└── LICENSE                      # MIT License
```

## Testing

Run the tests using Maven:

```bash
./mvnw test
```

Or:

```bash
mvn test
```

## Environment Profiles

The application supports multiple profiles:

- **dev**: Development environment
- **test**: Testing environment
- **prod**: Production environment

Set the active profile using the `SPRING_PROFILES_ACTIVE` environment variable.

## Dependencies

This project depends on the `talentos-commons` library (version 0.0.1-SNAPSHOT), which provides:
- Common DTOs (DomainDto, DomainOptionDto, CreateDomainDto)
- Response enumerations
- Shared utilities

Ensure this dependency is available in your local Maven repository or configured repository manager.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Giuseppe Falcone**
- Email: giuseppe.falcone@lutech.it

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Create a Pull Request

## Support

For issues, questions, or contributions, please open an issue on the GitHub repository.
