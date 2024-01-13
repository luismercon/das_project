# Use Eclipse Temurin 17 as the base image
FROM eclipse-temurin:17.0.3_7-jdk

# Install GCC and other necessary tools
RUN apt-get update && apt-get install -y gcc make

# Set the working directory in the container
WORKDIR /usr/src/myapp

# Copy the JAR file into the container at /usr/src/myapp
COPY target/online-compilation-0.0.1-SNAPSHOT.jar /usr/src/myapp/online-compilation.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java","-jar","online-compilation.jar"]