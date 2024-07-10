create schema if not exists wallet;

create table if not exists wallet.users (
    id                          serial primary key,
    first_name                  varchar(50) not null,
    middle_name                 varchar(50),
    last_name                   varchar(50) not null,
    phone_number                varchar(11) not null unique,
    email                       varchar(255) not null unique,
    birthdate                   date not null,
    password_hash               varchar not null,
    creation_time_stamp         timestamp not null,
    last_update_time_stamp      timestamp
)