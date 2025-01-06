#!/bin/bash

echo "Запуск Kafka и Zookeeper..."
docker-compose up -d kafka zookeeper

echo "Ожидание запуска Kafka и Zookeeper..."
sleep 15

echo "Создание топика 'airline' в Kafka..."
./create_kafka_topic.sh

echo "Kafka запущен и топик создан."
