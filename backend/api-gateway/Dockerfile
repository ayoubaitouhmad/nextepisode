FROM eclipse-temurin:17-jdk-alpine

# Install Maven
RUN apk add --no-cache maven

WORKDIR /app

# Copy pom first to cache dependencies
COPY pom.xml .

# Download dependencies (keeps future builds faster)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Expose gateway port
EXPOSE 8081

# Run in dev mode (NO JAR, direct source execution)
CMD ["mvn", "spring-boot:run"]

