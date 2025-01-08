#!/bin/bash

# Скрипт для развертывания выбранных сервисов
# Для развертывания всех сервисов просто запустить ./start_services.sh
# Для развертывания части сервисов надо перечислить их: ./start_services.sh postgres kafka mongo

echo "Начало процесса развертывания сервисов..."

SERVICES=("$@")

# Если параметры не заданы, разворачиваем все сервисы
if [ ${#SERVICES[@]} -eq 0 ]; then
  SERVICES=("postgres" "mongo" "kafka" "kafka_app" "spark" "airline")
fi

for SERVICE in "${SERVICES[@]}"; do
  case $SERVICE in
    postgres)
      echo "Развертывание Postgres..."
      ./postgres/deploy_postgres.sh
      ;;
    mongo)
      echo "Развертывание MongoDB..."
      ./mongo/deploy_mongodb.sh
      ;;
    kafka)
      echo "Развертывание Kafka..."
      ./kafka/deploy_kafka.sh
      ;;
    kafka_app)
      echo "Развертывание Kafka Application..."
      ./kafka/deploy_kafka_app.sh
      ;;
    spark)
      echo "Развертывание Spark..."
      ./spark/deploy_spark.sh
      ;;
    airline)
      echo "Развертывание Airline Application..."
      ./airline-app/deploy_airline_app.sh
      ;;
    *)
      echo "Неизвестный сервис: $SERVICE"
      ;;
  esac
done

echo "Процесс развертывания платформы приложений завершен."
