# Use Maven with Java 17 for building and running
FROM maven:3.9.5-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy the backend folder contents (where pom.xml is located)
COPY backend/ .

# Build the application
RUN mvn clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/crm-system-1.0.0.jar"]
