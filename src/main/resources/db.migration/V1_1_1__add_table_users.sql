create schema if not exists wallet;

create table if not exists wallet.users (
    id                        serial primary key,
    firstName                 varchar(50) not null,
    middleName                varchar(50) default null,
    lastName                  varchar(50) not null,
    phoneNumber               varchar(11) not null unique,
    email                     varchar(255) not null unique,
    birthdate                 date not null,
    passwordHash              varchar not null,
    creationTimeStamp         timestamp not null default current_timestamp,
    lastUpdateTimeStamp       timestamp default null
)