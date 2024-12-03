# STAGE 1: Build Stage
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere die Maven-Definitionen
COPY pom.xml ./

# Installiere Maven-Abhängigkeiten (für Caching)
RUN mvn dependency:go-offline -B

# Kopiere den Quellcode ins Image
COPY src ./src

# Baue das Projekt
RUN mvn clean package

# STAGE 2: Runtime Stage
FROM openjdk:17-jdk-slim AS runtime

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere das erzeugte JAR aus der Build-Stage
COPY --from=build /app/target/*.jar app.jar

# Exponiere den Standard-Spring-Boot-Port
EXPOSE 8080

# Startbefehl für die Spring-Boot-Anwendung
ENTRYPOINT ["java", "-jar", "app.jar"]