#!/bin/bash

# Конфигурация НТ этапа
WRK_PATH="./wrk" # Путь к исполняемому файлу wrk2
SCRIPT_PATH="/Users/axothy/IdeaProjects/airline-company-application/airline-company-load-testing/scripts/POST.lua"
URL="http://localhost:8082"
CONNECTIONS=64
DURATION=15
THREADS=4
RPS=5000

# Запуск wrk2 и захват вывода
OUTPUT=$($WRK_PATH -c $CONNECTIONS -d ${DURATION}s -t $THREADS -L -R $RPS -s $SCRIPT_PATH $URL 2>&1)

# Проверка успешности выполнения wrk2
if [ $? -ne 0 ]; then
    echo "Ошибка при выполнении wrk2."
    echo "$OUTPUT"
    exit 1
fi

# Извлечение Requests/sec
REQUESTS_PER_SEC=$(echo "$OUTPUT" | grep "Requests/sec" | awk '{print $2}')

# Извлечение Socket errors
SOCKET_ERRORS=$(echo "$OUTPUT" | grep "Socket errors" | awk '{print $3}')

# Проверка, что Requests/sec >= требуемого RPS
REACHED=$(echo "$REQUESTS_PER_SEC >= $RPS" | bc)

# Проверка, что ошибок нет
ERRORS_OK=true
if [ -n "$SOCKET_ERRORS" ] && [ "$SOCKET_ERRORS" -ne 0 ]; then
    ERRORS_OK=false
fi

# Логирование результатов
echo "Requests/sec: $REQUESTS_PER_SEC"
echo "Socket errors: $SOCKET_ERRORS"

# Окончательная проверка и выход
if [ "$REACHED" -eq 1 ] && [ "$ERRORS_OK" = true ]; then
    echo "Нагрузочное тестирование прошло успешно."
    exit 0
else
    echo "Нагрузочное тестирование не прошло."
    if [ "$REACHED" -ne 1 ]; then
        echo "Достигнутый RPS ($REQUESTS_PER_SEC) меньше требуемого ($RPS)."
    fi
    if [ "$ERRORS_OK" = false ]; then
        echo "Обнаружены ошибки сокетов: $SOCKET_ERRORS."
    fi
    exit 1
fi
