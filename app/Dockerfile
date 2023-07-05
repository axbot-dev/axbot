FROM gradle:8-alpine AS builder
WORKDIR /app
COPY . /app
RUN gradle bootJar

FROM eclipse-temurin:17-jre-alpine
ARG VERSION
ENV APP_VERSION=${VERSION}
EXPOSE 9999
WORKDIR /data
COPY --from=builder /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
