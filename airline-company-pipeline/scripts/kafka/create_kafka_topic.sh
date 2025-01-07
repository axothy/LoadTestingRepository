#!/bin/bash

echo "Создание топика 'airline'..."

docker-compose exec kafka kafka-topics --create
  --topic airline
  --bootstrap-server localhost:9092
  --replication-factor 1
  --partitions 1

echo "Список топиков в Kafka:"
docker-compose exec kafka kafka-topics --list --bootstrap-server localhost:9092
