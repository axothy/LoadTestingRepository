-- Инициализация генератора случайных чисел
math.randomseed(os.time())

function request()
    local headers = {}
    headers["Host"] = "localhost:8082"
    headers["Content-Type"] = "application/x-www-form-urlencoded"

    -- Генерация departureTownId от 0 до 6
    local departureTownId = math.random(0, 6)

    -- Генерация arrivalTownId от 0 до 6, не равного departureTownId
    local arrivalTownId = departureTownId
    while arrivalTownId == departureTownId do
        arrivalTownId = math.random(0, 6)
    end

    -- Генерация типа: 90% ECONOMY, 10% BUSINESS
    local rand = math.random()
    local ticketType = (rand < 0.9) and "ECONOMY" or "BUSINESS"

    -- Формирование тела запроса
    local body = string.format(
        "type=%s&departureTownId=%d&arrivalTownId=%d",
        ticketType,
        departureTownId,
        arrivalTownId
    )

    -- Формирование POST запроса к /ticket
    return wrk.format("POST", "/ticket", headers, body)
end

-- ./wrk -c 64 -d 5 -t 4 -L -R 100 -s /Users/axothy/IdeaProjects/airline-company-application/airline-company-load-testing/scripts/POST.lua http://localhost:8082