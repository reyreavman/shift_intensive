create table if not exists check_create_table(id bigserial not null);

create schema if not exists wallet;

create table if not exists wallet.users (
    id                          serial primary key,
    c_firstName                 varchar(50) not null check (c_firstName ~* '^[À-ß¨][à-ÿ¸]{1,50}$'),
    c_middleName                varchar(50) default null check (c_firstName ~* '^[À-ß¨][à-ÿ¸]{1,50}$'),
    c_lastName                  varchar(50) not null check (c_firstName ~* '^[À-ß¨][à-ÿ¸]{1,50}$'),
    c_phoneNumber               varchar(11) not null unique check (c_phoneNumber ~* '^7\d{10}$'),
    c_email                     varchar(255) not null unique check (c_email ~* ''),
    c_birthdate                 date not null,
    c_passwordHash              varchar(255) not null,
    c_registrationDateTime      timestamp not null default current_timestamp,
    c_lastUpdateDateTim         timestamp default null on update current_timestamp
)