FROM gradle:7.6.2-jdk17 AS build

WORKDIR /app

COPY . /app

RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/kafka-consumer-application-0.0.1-SNAPSHOT.jar kafka-app.jar

ENTRYPOINT ["java", "-jar", "kafka-app.jar"]
