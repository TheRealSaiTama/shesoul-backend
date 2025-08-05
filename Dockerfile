<<<<<<< HEAD
# ---- Stage 1: Build the application ----
# Use a Maven image that includes the JDK to build the project
FROM maven:3.8.5-openjdk-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Run the Maven command to build the project and create the JAR file
# This creates the /app/target/your-app-name.jar
RUN mvn clean package -DskipTests

# ---- Stage 2: Create the final, lightweight image ----
# Use a slim Java image because we only need to run the app, not build it
FROM openjdk:17-jdk-slim

# Copy ONLY the built JAR file from the 'builder' stage into the final image
COPY --from=builder /app/target/*.jar app.jar

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
=======
# Use Python 3.11 slim image
FROM python:3.11-slim

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1
ENV PYTHONPATH=/app

# Set work directory
WORKDIR /app

# Install system dependencies
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        build-essential \
        libpq-dev \
        curl \
    && rm -rf /var/lib/apt/lists/*

# Install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -r requirements.txt

# Copy project
COPY . .

# Create non-root user
RUN adduser --disabled-password --gecos '' appuser \
    && chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8000

# Health check
HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8000/health || exit 1

# Run the application
CMD ["gunicorn", "main:app", "--workers", "4", "--worker-class", "uvicorn.workers.UvicornWorker", "--bind", "0.0.0.0:8000"] 
>>>>>>> 9ddaa6500385302ab07c4ab1175ee7ee6dd04bb6
