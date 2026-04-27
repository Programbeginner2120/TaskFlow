# ---- Dev Stage (hot reload via Spring DevTools + volume-mounted source) ----
FROM eclipse-temurin:25-jdk-alpine AS dev
WORKDIR /app
ENV TZ=UTC

# Copy Gradle wrapper and build config to pre-download dependencies into the image layer.
# The full source tree will be supplied at runtime via a Docker volume mount.
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle/

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

EXPOSE 8080

# chmod ensures gradlew is executable even when volume-mounted from a Windows host.
# Spring DevTools (already on classpath via developmentOnly) will restart the app
# whenever compiled .class files change.
ENTRYPOINT ["sh", "-c", "chmod +x gradlew && ./gradlew bootRun --no-daemon"]

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

ENTRYPOINT ["java", "-Duser.timezone=UTC", "-jar", "app.jar"]
