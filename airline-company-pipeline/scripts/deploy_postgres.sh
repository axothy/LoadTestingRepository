#!/bin/bash

# Настройки PostgreSQL
POSTGRES_USER=postgres                    # Имя пользователя базы данных
POSTGRES_PASSWORD=mysecretpassword        # Пароль пользователя
POSTGRES_DB=airline                       # Название базы данных
POSTGRES_PORT=5432                        # Порт для подключения к PostgreSQL
CONTAINER_NAME=postgres_container         # Имя Docker-контейнера
ADDITIONAL_DB=airline

# Определение пути к директории данных PostgreSQL
DATA_DIR="$HOME/pgdata"

# Функция для вывода сообщений
function echo_info {
    echo -e "\033[1;34m[INFO]\033[0m $1"
}

function echo_error {
    echo -e "\033[1;31m[ERROR]\033[0m $1"
}

# Создание директории для данных PostgreSQL, если она не существует
if [ ! -d "$DATA_DIR" ]; then
    echo_info "Создаём директорию для данных PostgreSQL: $DATA_DIR"
    mkdir -p "$DATA_DIR"
    if [ $? -ne 0 ]; then
        echo_error "Не удалось создать директорию $DATA_DIR. Проверьте права доступа."
        exit 1
    fi
fi

# Проверка, запущен ли уже контейнер с заданным именем
if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo_info "Контейнер '$CONTAINER_NAME' уже запущен."
elif [ "$(docker ps -aq -f status=exited -f name=$CONTAINER_NAME)" ]; then
    echo_info "Контейнер '$CONTAINER_NAME' существует, но остановлен. Запускаем его..."
    docker start $CONTAINER_NAME
    if [ $? -ne 0 ]; then
        echo_error "Не удалось запустить контейнер '$CONTAINER_NAME'. Проверьте Docker."
        exit 1
    fi
else
    # Запуск нового контейнера PostgreSQL
    echo_info "Запускаем новый контейнер PostgreSQL..."
    docker run --name $CONTAINER_NAME \
        -e POSTGRES_USER=$POSTGRES_USER \
        -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
        -e POSTGRES_DB=$POSTGRES_DB \
        -p $POSTGRES_PORT:5432 \
        -v "$DATA_DIR":/var/lib/postgresql/data \
        -d postgres
# Проверка успешности запуска
    if [ $? -eq 0 ]; then
        echo_info "PostgreSQL успешно запущен в контейнере '$CONTAINER_NAME'."
        echo_info "Подключение: postgres://$POSTGRES_USER:$POSTGRES_PASSWORD@localhost:$POSTGRES_PORT/$POSTGRES_DB"
    else
        echo_error "Не удалось запустить контейнер PostgreSQL."
        exit 1
    fi
fi

# Функция для ожидания готовности PostgreSQL
function wait_for_postgres {
    echo_info "Ожидаем готовности PostgreSQL..."
    while true; do
        docker exec $CONTAINER_NAME pg_isready -U $POSTGRES_USER &> /dev/null
        if [ $? -eq 0 ]; then
            echo_info "PostgreSQL готов к подключенияению."
            break
        else
            echo_info "PostgreSQL ещё не готов. Ждём 2 секунды..."
            sleep 2
        fi
    done
}

# Вызов функции ожидания
wait_for_postgres

# Проверка существования дополнительной базы данных и создание, если необходимо
echo_info "Проверяем наличие базы данных '$ADDITIONAL_DB'..."

DB_EXISTS=$(docker exec -u postgres $CONTAINER_NAME psql -U $POSTGRES_USER -tAc "SELECT 1 FROM pg_database WHERE datname='$ADDITIONAL_DB';")

if [ "$DB_EXISTS" = "1" ]; then
    echo_info "База данных '$ADDITIONAL_DB' уже существует."
else
    echo_info "Создаём базу данных '$ADDITIONAL_DB'..."
    docker exec -u postgres $CONTAINER_NAME psql -U $POSTGRES_USER -c "CREATE DATABASE $ADDITIONAL_DB;"
    if [ $? -eq 0 ]; then
        echo_info "База данных '$ADDITIONAL_DB' успешно создана."
    else
        echo_error "Не удалось создать базу данных '$ADDITIONAL_DB'."
        exit 1
    fi
fi

echo_info "Развёртывание завершено успешно."
echo_info "Подключение к базе данных '$ADDITIONAL_DB': postgres://$POSTGRES_USER:$POSTGRES_PASSWORD@localhost:$POSTGRES_PORT/$ADDITIONAL_DB"