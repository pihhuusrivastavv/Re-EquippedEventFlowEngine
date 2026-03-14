# Used Java 21 JDK base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copied the built jar into the container
COPY target/Re-EquippedEventFlowEngine-0.0.1-SNAPSHOT.jar app.jar

# Exposed the port Spring Boot app runs on
EXPOSE 8080

# Entry point to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]