# Iglulivery

Iglulivery is a delivery management application built with Spring Boot. It provides a RESTful API for managing users, authentication, and delivery orders.

## Features

- **User Authentication**: Secure registration and login using JSON Web Tokens (JWT).
- **Order Management**: Create, view, accept, and update the status of delivery orders.
- **Database Integration**: Data persistence using PostgreSQL.
- **API Documentation**: Interactive API documentation powered by Swagger/OpenAPI.
- **Containerization**: Docker support for easy deployment and local development.

## Technologies Used

- **Java 21**
- **Spring Boot 3.x**
  - Spring WebMVC
  - Spring Data JPA
  - Spring Security
  - Spring Validation
- **PostgreSQL**
- **JWT (JSON Web Tokens)** for authentication
- **Maven** for dependency management
- **Docker & Docker Compose**
- **Swagger/OpenAPI** (`springdoc-openapi`)

## Prerequisites

To run this application, you will need:

- **Java 21** (if running locally without Docker)
- **Maven** (optional, wrapper is included)
- **Docker** and **Docker Compose** (for containerized execution)

## Environment Variables

The application requires the following environment variables. If you are using Docker Compose, you can define these in a `.env` file in the root directory or export them in your shell:

- `DB_USER` (e.g., `postgres`)
- `DB_PASSWORD` (e.g., `mysecretpassword`)
- `JWT_SECRET_KEY` (e.g., `my-super-secret-key-that-is-very-long`)

If running locally without Docker Compose, default values are provided in `application.properties`, but you should override them for production or custom setups:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET_KEY`

## Getting Started

### Running with Docker Compose (Recommended)

1. Ensure Docker is running.
2. Set the necessary environment variables:
   ```bash
   export DB_USER=postgres
   export DB_PASSWORD=yourpassword
   export JWT_SECRET_KEY=yoursupersecretjwtkeythatislongenough
   ```
3. Build and start the containers:
   ```bash
   docker-compose up --build
   ```
   This will start both the PostgreSQL database and the Spring Boot application on port `8080`.

### Running Locally with Maven

1. Start a PostgreSQL instance locally and create a database named `iglulivery_db`.
2. Set the environment variables in your terminal:
   ```bash
   export DB_USERNAME=postgres
   export DB_PASSWORD=yourpassword
   export JWT_SECRET_KEY=yoursupersecretjwtkeythatislongenough
   export DB_URL=jdbc:postgresql://localhost:5432/iglulivery_db
   ```
3. Build and run the application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

## API Documentation

The application uses Swagger/OpenAPI to document its REST endpoints. Once the application is running, you can access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```
*(Note: The exact path depends on your `springdoc` configuration, it may also be available at `/swagger-ui/index.html`)*

## Endpoints Overview

### Authentication (`/api/auth`)
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate a user and receive a JWT

### Orders (`/api/orders`)
- `POST /api/orders` - Create a new order
- `GET /api/orders` - Retrieve all orders
- `GET /api/orders/available` - Retrieve available orders
- `GET /api/orders/{id}` - Retrieve a specific order by ID
- `PATCH /api/orders/{id}/status` - Update the status of an order
- `PATCH /api/orders/{id}/accept` - Assign/accept an order

## License

This project is open-source and available under its respective license.
