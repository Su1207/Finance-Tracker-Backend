# ---- Stage 1: Build ----
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jdk

# Set working directory in final image
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Render uses $PORT env internally, so this is mostly symbolic)
EXPOSE 8081

# Use the dynamic port Render gives you
ENV PORT=8081

# Entry point
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
