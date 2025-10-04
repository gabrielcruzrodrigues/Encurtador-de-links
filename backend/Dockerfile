FROM eclipse-temurin:21-jdk-jammy

RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

WORKDIR /app
COPY . .

RUN mvn -N io.takari:maven:wrapper
RUN chmod +x ./mvnw && ./mvnw clean install -DskipTests
ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=prod", "target/encurtador-de-links-0.0.1-SNAPSHOT.jar" ]