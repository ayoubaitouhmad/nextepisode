FROM maven:3.9-eclipse-temurin-21-alpine
WORKDIR /app

# tools for file watching (optional but better)
RUN apk add --no-cache bash inotify-tools

# cache deps
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# dev script
COPY dev.sh /app/dev.sh
RUN chmod +x /app/dev.sh

CMD ["/app/dev.sh"]
