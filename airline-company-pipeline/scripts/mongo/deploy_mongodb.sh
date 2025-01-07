#!/bin/bash

echo "Запуск MongoDB..."
docker-compose up -d mongodb

echo "Ожидание запуска MongoDB..."
sleep 5

echo "Создание базы данных 'airline' и коллекции 'ticket_statistics' в MongoDB..."
./create_mongo_collection.sh

echo "MongoDB запущен и настроен."
