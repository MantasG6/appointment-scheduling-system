# Appointment Scheduling System

A RESTful web API built with Java and Spring Boot that allows users to manage service provider availability and schedule
appointments. The application uses PostgreSQL for persistent storage and Testcontainers for integration testing.

## 🚀 Features

- User Registration & Authentication (Keycloak managed)
- Role-based Access Control: Provider, Client
- Service creation & management
- Time slot availability management
- Appointment booking, updating, and cancellation
- Conflict detection for overlapping appointments
- API documentation with Swagger
- Integration testing with Testcontainers
- PostgreSQL as the primary database

## 🛠️ Tech Stack

- Java 21+
- Spring Boot 3.x
- Spring Data JPA + Hibernate
- Keycloak for Authentication
- Spring Security + JWT
- PostgreSQL
- Testcontainers
- Docker
- Maven
- Lombok
- Swagger (Springdoc OpenAPI)

## 🧱 Project Structure

```
src
├── main
│   ├── java
│   │   └── com.mantas.appointments
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── repository
│   │       └── security
│   │       ├── service
│   └── resources
│       └── application.yml
└── test
    └── java
        └── com.mantas.appointments
            ├── controller
            ├── integration
            ├── security
            ├── service
            └── utils
```

## ⚙️ Setup Instructions

### Prerequisites

- Java 21+
- Maven
- Docker (for PostgreSQL container and testing with Testcontainers)

### Running the App Locally

#### 1. Clone the repository

```
git clone https://github.com/MantasG6/appointment-scheduling-system.git
cd appointment-scheduling-system
```

#### 2. Set up Keycloak (Not required for local testing)

This [guide](https://www.baeldung.com/spring-boot-keycloak) was used to set up Keycloak for authentication.
Follow the steps to configure Keycloak with the necessary realms, clients, and roles.

#### 3. Configure the application

Add `src/main/resources/secrets.yml` with your DB credentials.

```
spring:
  datasource:
    username: your_db_username
    password: your_db_password
```

#### 4. Add Docker secrets

Create a `docker-secrets` directory in the root of the project:

- Add a file name `db_password.txt` with your PostgreSQL password.
- Add a file named `db_user.txt` with your PostgreSQL username.
- Add a file named `keycloak_admin.txt` with your Keycloak admin username.
- Add a file named `keycloak_admin_password.txt` with your Keycloak admin password.

#### 5. Run the application

```
./mvnw spring-boot:run
```

#### 6. Access Swagger UI

Visit http://localhost:8080/swagger-ui.html

### Running Tests

```
./mvnw test
```

Tests will use Testcontainers to spin up a PostgreSQL instance dynamically.

## 🧪 Testing with Testcontainers

- Isolated PostgreSQL container spun up before each test suite
- Ensures tests run in clean and reproducible environments
- Integration tests for REST endpoints and JPA repositories

## 🔐 Keycloak Endpoints

Keycloak is used for user authentication and management. Below is the endpoint to authenticate and obtain a JWT token,
which should be used later for all application APIs:

| Method | Endpoint                                             | Description              |
|--------|------------------------------------------------------|--------------------------|
| POST   | `/auth/realms/{realm}/protocol/openid-connect/token` | Authenticate and get JWT |

## 🗄️ API Endpoints

| Method | Endpoint                | Description       |
|--------|-------------------------|-------------------|
| GET    | `/api/v1/services`      | Get all services  |
| GET    | `/api/v1/services/{id}` | Get service by ID |
| POST   | `/api/v1/services`      | Create a service  |
| PUT    | `/api/v1/services/{id}` | Update a service  |
| DELETE | `/api/v1/services/{id}` | Delete a service  |
