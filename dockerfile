# Use a lightweight OpenJDK base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy built JAR (see step 2)
COPY target/*.jar app.jar

# Expose the port (Render uses environment variable PORT)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
