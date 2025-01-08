#!/bin/bash

echo "Запуск приложения Airline..."
docker-compose up -d airline-app

echo "Ожидание запуска Airline приложения..."
sleep 10
