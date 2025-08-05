# ---- Stage 1: Build the application ----
# Use a Maven image that includes the JDK to build the project
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first for better caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies (for better Docker layer caching)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Verify JAR exists and list contents
RUN ls -la target/ && find target -name "*.jar" -type f

# ---- Stage 2: Create the final, lightweight image ----
# Use a slim Java image because we only need to run the app, not build it
FROM eclipse-temurin:17-jre-alpine

# Add a non-root user
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder /app/target/v1update-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appgroup /app/app.jar

# Switch to non-root user
USER appuser

# Expose the port
EXPOSE 8080

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]
