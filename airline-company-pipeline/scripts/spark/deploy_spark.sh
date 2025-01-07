#!/bin/bash

docker-compose build spark-app

echo "Запуск потокового Spark приложения..."
docker-compose up -d spark-app

echo "Ожидание потокового Spark приложения..."
sleep 5
