
FROM maven:3.8.5-openjdk-17 AS build


# Copy only pom.xml to cache dependencies
COPY . .
RUN mvn clean package -DskipTests

# Copy source code and build JAR
COPY src ./src
RUN mvn clean package -DskipTests

# -------- Step 2: Run the JAR --------
FROM openjdk:17.0.1-jdk-slim
COPY --from=build/target/orientationApp-0.0.1-SNAPSHOT.jar orientationApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "orientationApp.jar"]
