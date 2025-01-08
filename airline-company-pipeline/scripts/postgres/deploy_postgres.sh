#!/bin/bash

echo "Запуск Postgres базы данных..."
docker-compose up -d postgres

echo "Ожидание запуска Postgres..."
sleep 5
