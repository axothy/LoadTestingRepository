#!/bin/bash

echo "Создание базы данных и коллекции в MongoDB..."

docker-compose exec mongodb mongosh --eval '
use ticket_db;
db.createCollection("ticket_statistics");
print("Базы данных:");
show dbs;
print("Коллекции в ticket_db:");
show collections;
'
