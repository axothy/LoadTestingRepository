# Используем официальный образ OpenJDK для Java 21
FROM openjdk:21-jdk as build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируйте файлы в контейнер
COPY build/libs/airline-company-application-0.0.1-SNAPSHOT.jar /app/airline-company-application.jar

RUN mkdir -p /usr/airline/entities

# Команда запуска
CMD ["/bin/sh", "-c", "java -jar /app/airline-company-application.jar"]

#запускать как docker build -t airline-company-application .