FROM gradle:8-alpine AS builder
WORKDIR /app
COPY . /app
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /app/tools/opentelemetry-javaagent.jar
RUN gradle bootJar

FROM eclipse-temurin:17-jre
LABEL org.opencontainers.image.source=https://github.com/axbot-dev/axbot
LABEL org.opencontainers.image.description="axbot app"
LABEL org.opencontainers.image.licenses="GPL-3.0"
ARG VERSION
ENV APP_VERSION=${VERSION}
ENV JAVA_TOOL_OPTIONS="-javaagent:/app/tools/opentelemetry-javaagent.jar"
ENV OTEL_TRACES_EXPORTER="none"
ENV OTEL_METRICS_EXPORTER="none"
ENV OTEL_LOGS_EXPORTER="none"
ENV OTEL_SERVICE_NAME="axbot-app"
EXPOSE 9999
WORKDIR /data
COPY --from=builder /app/build/libs/*.jar /app/app.jar
COPY --from=builder /app/tools/opentelemetry-javaagent.jar /app/tools/opentelemetry-javaagent.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
