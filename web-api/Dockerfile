FROM gradle:7.6.2-jdk17 AS build

WORKDIR /app

COPY . /app

RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/web-api-0.0.1-SNAPSHOT.jar airline-app.jar

ENTRYPOINT ["java", "-jar", "airline-app.jar"]
