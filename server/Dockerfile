FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
#COPY build/settings.xml /root/.m2/settings.xml
COPY . /app
RUN mvn clean package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /data
COPY --from=builder /app/axbot-api/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
