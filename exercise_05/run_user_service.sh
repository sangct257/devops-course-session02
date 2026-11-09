#!/bin/bash

# Khởi chạy container user-service mount thư mục build/libs từ máy host
docker run -d \
  --name quickbite-user \
  -p 8081:8081 \
  -v "$(pwd)/build/libs:/app" \
  -w /app \
  --add-host=host.docker.internal:host-gateway \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=1234 \
  -e SERVER_PORT=8081 \
  eclipse-temurin:21-jre-alpine \
  java -jar user-service-0.0.1-SNAPSHOT.jar