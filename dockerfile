# -------- Stage 1: Build from source using Maven ----------
    FROM maven:3.9.5-eclipse-temurin-17 AS builder

    WORKDIR /app
    
    # Copy source code
    COPY . .
    
    # Build the application (skip tests if needed)
    RUN mvn clean package -DskipTests
    
    # -------- Stage 2: Run the built JAR with JDK ----------
    FROM eclipse-temurin:17-jdk-alpine
    
    WORKDIR /app
    
    # Copy the JAR from the builder stage
    COPY --from=builder /app/target/*.jar app.jar
    
    # Render sets PORT dynamically — use it in Spring Boot
    EXPOSE 8080
    
    # Run the Spring Boot application
    CMD ["java", "-jar", "app.jar"]
    