
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom.xml to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build JAR
COPY src ./src
RUN mvn clean package -DskipTests

# -------- Step 2: Run the JAR --------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
