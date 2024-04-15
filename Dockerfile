FROM openjdk:17-jdk-slim
WORKDIR /pinService
COPY . .
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/pinService-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080