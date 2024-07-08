# Use the Java runtime image for OpenJDK 21
FROM openjdk:21

# Set the maintainer information
LABEL maintainer="didit@dwidi.dev"

# Expose port 8080 to the outside world
EXPOSE 8080

# Copy the application's jar file into the container
COPY target/store-0.0.1-SNAPSHOT.jar /app.jar

# Run the jar file
CMD ["java", "-jar", "/app.jar"]
