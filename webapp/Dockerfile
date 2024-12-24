FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY . .
RUN ./gradlew build -x test
ENTRYPOINT ["java", "-jar", "build/libs/captiveportal-0.0.1-SNAPSHOT.jar"]