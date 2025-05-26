# Smart Appointment Scheduling System

A RESTful web API built with Java and Spring Boot that allows users to manage service provider availability and schedule appointments. The application uses PostgreSQL for persistent storage and Testcontainers for integration testing.
## 🚀 Features
- User Registration & Authentication (JWT-based)
- Role-based Access Control: Admin, ServiceProvider, Client
- Service creation & management
- Time slot availability management
- Appointment booking, updating, and cancellation
- Conflict detection for overlapping appointments
- API documentation with Swagger
- Integration testing with Testcontainers
- PostgreSQL as the primary database

## 🛠️ Tech Stack
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Testcontainers
- Docker (optional for containerized deployments)
- Maven
- Lombok
- Swagger (Springdoc OpenAPI)

## 🧱 Project Structure
```
src
├── main
│   ├── java
│   │   └── com.example.appointments
│   │       ├── controller
│   │       ├── model
│   │       ├── repository
│   │       ├── service
│   │       └── security
│   └── resources
│       ├── application.properties
│       └── ...
└── test
    └── java
        └── com.example.appointments
            └── integration
```

## ⚙️ Setup Instructions
### Prerequisites
- Java 17+
- Maven
- Docker (for PostgreSQL container or full containerization)

### Running the App Locally
#### 1. Clone the repository
```
git clone https://github.com/MantasG6/appointment-scheduling-system.git
cd appointment-scheduling-system
```

#### 2. Configure the application

Edit `src/main/resources/application.properties` with your local PostgreSQL credentials or use Docker to run a containerized DB.

#### 3. Run the application
```
./mvnw spring-boot:run
```

#### 4. Access Swagger UI

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

## 🗄️ API Endpoints (sample)
| Method | Endpoint               | Description                      |
| ------ | ---------------------- | -------------------------------- |
| POST   | `/api/auth/register`   | Register a new user              |
| POST   | `/api/auth/login`      | Authenticate and get JWT         |
| GET    | `/api/services`        | List available services          |
| POST   | `/api/availability`    | Define availability for provider |
| POST   | `/api/appointments`    | Book an appointment              |
| GET    | `/api/appointments/me` | View user's appointments         |