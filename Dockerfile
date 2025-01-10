# Use a base image with JDK 17 installed
FROM gradle:8.3.0-jdk17-alpine


ADD . /duo-app
WORKDIR /duo-app


# Build the application
# RUN ./gradlew build --no-daemon

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Run the Spring Boot application
CMD ["./gradlew", "bootRun"]