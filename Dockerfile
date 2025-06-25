# Stage 1: Build the app with Gradle and JDK 21
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Gradle wrapper and config first
COPY gradlew .
COPY gradle gradle
COPY build.gradle* .
COPY settings.gradle* .

# Pre-fetch dependencies
RUN ./gradlew --no-daemon dependencies

# Copy source files
COPY . .

# Run tests and build the JAR
RUN ./gradlew clean build --no-daemon

# Stage 2: Run the app
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Install netcat (nc) for waiting on Postgres
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 4000

# Wait for the database before starting the app
CMD ["sh", "-c", "until nc -z db 5432; do echo 'Waiting for Postgres...'; sleep 2; done && java -jar app.jar"]
