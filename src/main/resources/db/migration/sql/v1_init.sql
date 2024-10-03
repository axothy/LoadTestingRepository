create table if not exists countries
(
    id                   serial primary key,
    country_name         varchar(70) not null
);

create table if not exists towns
(
    id                   serial primary key,
    town_name            varchar(70) not null,
    airport_code         varchar(70) unique not null,
    country_id           integer references countries(id)
);

create table if not exists airbuses
(
    id                 serial primary key,
    airbus_model       varchar(70) not null,
    date_of_expluatation  date not null
);

create table if not exists seats
(
    id                 serial primary key,
    seat_class         varchar(70) not null,
    seat_number        integer not null,
    available          boolean not null,
    seat_type          varchar(70) not null,
    airbus_id          integer references airbuses(id)
);

create table if not exists flights
(
    id                 serial primary key,
    distance_km        varchar(70) not null,
    start_date_time    timestamp not null,
    end_date_time      timestamp not null,
    airbus_id          integer references airbuses(id),
    departure_town_id  integer references towns(id),
    arrival_town_id    integer references towns(id)
);

create table if not exists passengers
(
    id                 serial primary key,
    first_name         varchar(70) not null,
    last_name          varchar(70) not null
);

create table if not exists tickets
(
    id                 serial primary key,
    ticket_type        varchar(70) not null,
    baggage            boolean not null,
    flight_id          integer unique references flights(id),
    seat_id            integer unique references seats(id),
    passenger_id       integer unique references passengers(id)
);


-- Страны
insert into countries (country_name) values ('RUSSIA');
insert into countries (country_name) values ('BELARUS');
insert into countries (country_name) values ('KAZAKHSTAN');

-- Города/Аэропорты
insert into towns (town_name, airport_code, country_id) values ('Moscow', 'SVO', (select id from countries where country_name = 'RUSSIA'));
insert into towns (town_name, airport_code, country_id) values ('Saint-Petersburg', 'LED', (select id from countries where country_name = 'RUSSIA'));
insert into towns (town_name, airport_code, country_id) values ('Yekaterinburg', 'SVX', (select id from countries where country_name = 'RUSSIA'));

insert into towns (town_name, airport_code, country_id) values ('Minsk', 'MSQ', (select id from countries where country_name = 'BELARUS'));
insert into towns (town_name, airport_code, country_id) values ('Gomel', 'GME', (select id from countries where country_name = 'BELARUS'));

insert into towns (town_name, airport_code, country_id) values ('Almaty', 'ALA', (select id from countries where country_name = 'KAZAKHSTAN'));
insert into towns (town_name, airport_code, country_id) values ('Nur-Sultan', 'NQZ', (select id from countries where country_name = 'KAZAKHSTAN'));

-- Самолеты
insert into airbuses (airbus_model, date_of_expluatation) values ('Sukhoi SuperJet 100', '1991-01-01');
insert into airbuses (airbus_model, date_of_expluatation) values ('Airbus A320 NEO', '1991-01-01');
insert into airbuses (airbus_model, date_of_expluatation) values ('Airbus A319', '1991-01-01');
insert into airbuses (airbus_model, date_of_expluatation) values ('Airbus A321', '1991-01-01');
insert into airbuses (airbus_model, date_of_expluatation) values ('Boeing 777-300ER', '1991-01-01');

-- Места в самолетах
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 1, true, 'DEFAULT', (select id from airbuses where id = 1));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 2, true, 'DEFAULT', (select id from airbuses where id = 1));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 3, true, 'DEFAULT', (select id from airbuses where id = 1));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('BUSINESS', 4, true, 'DEFAULT', (select id from airbuses where id = 1));

insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 1, true, 'DEFAULT', (select id from airbuses where id = 2));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 2, true, 'DEFAULT', (select id from airbuses where id = 2));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 3, true, 'DEFAULT', (select id from airbuses where id = 2));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('BUSINESS', 4, true, 'DEFAULT', (select id from airbuses where id = 2));

insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 1, true, 'DEFAULT', (select id from airbuses where id = 3));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 2, true, 'DEFAULT', (select id from airbuses where id = 3));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 3, true, 'DEFAULT', (select id from airbuses where id = 3));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('BUSINESS', 4, true, 'DEFAULT', (select id from airbuses where id = 3));

insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 1, true, 'DEFAULT', (select id from airbuses where id = 4));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 2, true, 'DEFAULT', (select id from airbuses where id = 4));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 3, true, 'DEFAULT', (select id from airbuses where id = 4));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('BUSINESS', 4, true, 'DEFAULT', (select id from airbuses where id = 4));

insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 1, true, 'DEFAULT', (select id from airbuses where id = 5));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 2, true, 'DEFAULT', (select id from airbuses where id = 5));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('ECONOM', 3, true, 'DEFAULT', (select id from airbuses where id = 5));
insert into seats (seat_class, seat_number, available, seat_type, airbus_id) values ('BUSINESS', 4, true, 'DEFAULT', (select id from airbuses where id = 5));

-- Человеки
insert into passengers (first_name, last_name) values ('Ivan', 'Ivanov');
insert into passengers (first_name, last_name) values ('Petr', 'Petrov');
insert into passengers (first_name, last_name) values ('Pavel', 'Pavlov');
insert into passengers (first_name, last_name) values ('Anastasia', 'Ivanova');
insert into passengers (first_name, last_name) values ('Julia', 'Smith');





