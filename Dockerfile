# Stage 1 eke nama "builder" kiyala wenas kala
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Methana --from=builder kiyala wenas kala
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]