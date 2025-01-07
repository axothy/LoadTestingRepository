#!/bin/bash

echo "Запуск Kafka приложения, которое вычитывает данные из топика и поставляет в Mongo..."
docker-compose up -d kafka-to-mongo-app

echo "Ожидание запуска Kafka приложения..."
sleep 5
