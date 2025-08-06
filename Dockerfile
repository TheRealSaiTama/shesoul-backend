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

# Expose the port (Railway will override this with PORT env var)
EXPOSE 8080

# Set environment variable for port
ENV PORT=8080

# JVM Memory settings for Railway (optimize for container)
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0 -XX:+UseG1GC -XX:+UseStringDeduplication"

# Set Spring profile for Railway
ENV SPRING_PROFILES_ACTIVE=railway

# Set the command to run the application with explicit server port and JVM options
ENTRYPOINT ["sh", "-c", "echo '--- Environment Variables ---' && printenv && echo '--- Starting Application ---' && java $JAVA_OPTS -Dserver.port=${PORT} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]
