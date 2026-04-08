# ---- Build Stage ----
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

# Copy Gradle wrapper and config files first for layer caching
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle/

# Make gradlew executable and pre-download dependencies
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Copy source code and build the fat JAR
COPY src src/
RUN ./gradlew bootJar --no-daemon

# ---- Runtime Stage ----
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app

# Run as non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
