FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Receitas de Crochê"
LABEL description="API de Receitas de Crochê - Spring Boot + MongoDB"
LABEL version="1.0.0"

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-Xms256m -Xmx512m" \
    MONGODB_URI="mongodb://mongo:27017/crochet_recipes" \
    SERVER_PORT=8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]