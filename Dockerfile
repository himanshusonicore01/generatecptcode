# Build Stage
FROM maven:3.8.4-openjdk-17 as build
LABEL maintainer="himanshu@coreteams.us"
RUN mkdir /home/src
COPY . /home/app/
WORKDIR /home/app
RUN mvn clean package

# Run Stage
FROM maven:3.8.4-openjdk-17
RUN mkdir /home/src
WORKDIR /home/app

# Copy the JAR file built in the previous stage
COPY --from=build /home/app/target/cptcode-0.0.1-SNAPSHOT.jar .

# Create volume for external logs
VOLUME ["/logs"]

# Expose port for external access
EXPOSE 6001

# Set the default environment variable for Spring profile
ENV SPRING_ACTIVE_PROFILES=default

# Run the application
CMD ["java", "-XX:+UseG1GC", "-XX:+UseStringDeduplication", "-Djdk.tls.client.protocols=TLSv1.2", "-Dspring.profiles.active=${SPRING_ACTIVE_PROFILES}", "-jar", "cptcode-0.0.1-SNAPSHOT.jar"]
