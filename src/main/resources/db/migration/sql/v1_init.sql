create table if not exists towns
(
    id                   serial primary key,
    town_name            varchar(70) not null
);

create table if not exists tickets
(
    id                 serial primary key,
    ticket_type        varchar(70) not null,
    departure_town_id  integer references towns(id),
    arrival_town_id    integer references towns(id)
);


-- Города/Аэропорты
insert into towns (town_name) values ('Moscow');
insert into towns (town_name) values ('Saint-Petersburg');
insert into towns (town_name) values ('Yekaterinburg');

insert into towns (town_name) values ('Minsk');
insert into towns (town_name) values ('Gomel');

insert into towns (town_name) values ('Almaty');
insert into towns (town_name) values ('Nur-Sultan');
