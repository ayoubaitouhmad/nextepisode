FROM eclipse-temurin:17-jdk-alpine

# Install Maven
RUN apk add --no-cache maven

WORKDIR /app

# Copy pom first to cache dependencies
COPY pom.xml .

# Add opentelemetry
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.8.0/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar
# Download dependencies (keeps future builds faster)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Expose gateway port
EXPOSE 8080

# Run in dev mode (NO JAR, direct source execution)
CMD ["mvn", "spring-boot:run"]

