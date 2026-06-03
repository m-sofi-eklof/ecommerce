FROM eclipse-temurin:24-jdk-alpine AS BUILD
WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=BUILD /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
