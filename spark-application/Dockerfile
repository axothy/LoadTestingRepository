FROM gradle:7.6.2-jdk11 AS build

WORKDIR /app

COPY . /app

RUN gradle build --no-daemon

FROM bitnami/spark:3.5.4

ENV HOME=/app

RUN mkdir -p /tmp/.ivy && chmod -R 777 /tmp/.ivy

WORKDIR /app

USER root

RUN adduser --disabled-password --gecos '' --uid 1000 sparkuser

RUN mkdir -p /app/data

VOLUME ["/app/data"]

USER sparkuser

ENV SPARK_USER=sparkuser

COPY --from=build /app/build/libs/spark-application-0.0.1-SNAPSHOT.jar spark-app.jar

ENTRYPOINT ["/opt/bitnami/spark/bin/spark-submit", "--class", "ru.axothy.FileToKafkaApp", "--conf", "spark.jars.ivy=/tmp/.ivy","/app/spark-app.jar"]
