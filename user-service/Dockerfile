#stage 1: build
FROM gradle:9.8.2-jdk21-alpine AS build
WORKDIR /app
COPY src .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon

#stage 2: run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]