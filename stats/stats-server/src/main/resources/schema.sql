create table hits
(
    id        serial primary key,
    app       varchar(255) not null,
    uri       varchar(255) not null,
    ip       varchar(255) not null,
    timestamp TIMESTAMP    not null
);

