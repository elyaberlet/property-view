# Property View

A RESTful Spring Boot application for managing hotel information.

The application provides REST API endpoints for creating hotels, retrieving hotel information, searching hotels by different criteria, managing amenities, and viewing hotel statistics.

## Technologies

- Java 21
- Spring Boot 3.4.0
- Maven
- Spring Data JPA
- H2 Database
- Liquibase
- MapStruct
- Lombok
- Springdoc OpenAPI / Swagger UI
- JUnit 5
- Mockito
- Spring Boot Test

## Features

- Retrieve a list of all hotels with summary information.
- Retrieve detailed information about a hotel.
- Search hotels by name, brand, city, country, and amenities.
- Create new hotels.
- Add amenities to existing hotels.
- Generate histograms grouped by brand, city, country, or amenities.
- Validate incoming requests.
- Handle exceptions globally.
- Manage database schema using Liquibase migrations.

## Project Structure

The application follows a layered architecture:

- **Controller** — handles HTTP requests and responses.
- **Service** — contains business logic.
- **Repository** — provides database access.
- **Entity** — represents database entities.
- **DTO** — defines request and response objects.
- **Mapper** — converts entities to DTOs and vice versa.
- **Exception** — contains custom exceptions and global exception handling.
- **Specification** — provides dynamic search criteria.

## Configuration

| Property | Value |
|---|---|
| Java version | 21 |
| Server port | 8092 |
| Context path | `/property-view` |
| Database | H2 |
| Database migrations | Liquibase |

## Running the Application

Make sure Java 21 and Maven are installed.

Clone the repository:

```bash
git clone https://github.com/elyaberlet/property-view.git
```

Navigate to the project directory:

```bash
cd property-view
```

Run the application:

```bash
mvn spring-boot:run
```

The application will start at:

```text
http://localhost:8092/property-view
```

## API Endpoints

All endpoints use the `/property-view` context path.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/hotels` | Retrieve all hotels with summary information |
| GET | `/hotels/{id}` | Retrieve detailed hotel information |
| GET | `/search` | Search hotels by specified criteria |
| POST | `/hotels` | Create a new hotel |
| POST | `/hotels/{id}/amenities` | Add amenities to a hotel |
| GET | `/histogram/{param}` | Get hotel statistics grouped by a parameter |

### Search Parameters

The `/search` endpoint supports the following optional query parameters:

- `name`
- `brand`
- `city`
- `country`
- `amenities`

Example:

```http
GET /property-view/search?city=Minsk
```

### Histogram Parameters

The `/histogram/{param}` endpoint supports:

- `brand`
- `city`
- `country`
- `amenities`

Example:

```http
GET /property-view/histogram/city
```

## Swagger / OpenAPI Documentation

The project uses Swagger UI through Springdoc OpenAPI to provide interactive API documentation.

After starting the application, open:

**Swagger UI:**

```text
http://localhost:8092/property-view/swagger-ui/index.html
```

**OpenAPI specification:**

```text
http://localhost:8092/property-view/v3/api-docs
```

Swagger UI allows you to:

- View all available API endpoints.
- Inspect request parameters and response schemas.
- Review HTTP methods and response codes.
- Execute API requests directly from the browser.

## Database Migrations

Liquibase is used to manage database schema changes.

Migration files are located in:

```text
src/main/resources/db/changelog/
```

The database schema is automatically initialized when the application starts.

## Testing

The project includes:

- Unit tests for service-layer business logic.
- Controller tests for REST API endpoints.
- Integration tests for application functionality and database operations.

Run all tests using:

```bash
mvn test
```

## License

This project was developed as part of a Java Advanced course assignment.