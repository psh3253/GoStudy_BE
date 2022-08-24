FROM openjdk:11-jdk-slim-buster
COPY build/libs/GoStudy_BE-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]