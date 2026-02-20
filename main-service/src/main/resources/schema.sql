drop table if exists compilations_events;
drop table if exists requests;
drop table if exists events;
drop table if exists users;
drop table if exists categories;
drop table if exists compilations;
drop table if exists locations;

create table users
(
    id    bigserial primary key,
    name  varchar(250) not null,
    email varchar(254) not null unique
);
create table categories
(
    id   bigserial primary key,
    name varchar(50) not null unique
);
create table locations
(
    id  bigserial primary key,
    lat float not null,
    lon float not null
);
create table events
(
    id                 bigserial primary key,
    title              varchar(120)                not null,
    annotation         varchar(2000)               not null,
    description        varchar(7000)               not null,
    event_date         timestamp without time zone not null,
    created_on         timestamp without time zone not null,
    published_on       timestamp without time zone,
    paid               boolean                     not null,
    participant_limit  integer                     not null,
    request_moderation boolean                     not null,
    state              varchar(10)                 not null,
    confirmed_requests bigint                      not null,
    category_id        bigint references categories (id),
    initiator_id       bigint references users (id),
    location_id        bigint references locations (id),
    views              bigint                      not null
);
create table requests
(
    id           bigserial primary key,
    created      timestamp without time zone not null,
    status       varchar(10)                 not null,
    event_id     bigint references events (id),
    requester_id bigint references users (id),
    constraint unique_request unique (event_id, requester_id)
);
create table compilations
(
    id     bigserial primary key,
    pinned boolean     not null,
    title  varchar(50) not null
);
create table compilations_events
(
    compilation_id bigint not null references compilations (id),
    event_id       bigint not null references events (id),
    primary key (compilation_id, event_id)
);
