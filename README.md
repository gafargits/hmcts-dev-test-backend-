# HMCTS Dev Test Backend
This will be the backend for the brand new HMCTS case management system.
## Getting Started

### Prerequisites

- Java 21

- Docker & Docker Compose (if using containerized setup)

- Your preferred IDE (e.g., IntelliJ, VS Code)

You should be able to run `./gradlew build` to start with to ensure it builds successfully. Then from that you
can run the service in IntelliJ (or your IDE of choice) or however you normally would.

## Build the Project

To build the application, run:

`./gradlew build`

This will compile the code, run tests, and package the application.

## Run Locally

You can run the application using your IDE by executing the main() method in the application class (e.g., DevApplication.java).

Alternatively, you can run the JAR directly:

`java -jar build/libs/<your-jar-file>.jar`

The application will start on http://localhost:4000.

## Run with Docker

Build and start the application using Docker Compose:

`docker compose up --build`

This will:

- Build the backend application JAR

- Start a PostgreSQL database

- Start the backend server

The backend service waits for the database to be ready before starting.

