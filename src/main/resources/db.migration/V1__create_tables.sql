create table if not exists check_create_table(id bigserial not null);

create schema if not exists wallet;

create table if not exists wallet.users (
    id                          serial primary key,
    c_firstName                 v
    c_middleName
    c_lastName
    c_phoneNumber
    c_email
    c_birthdate
    c_passwordHash
    c_registrationDateTime
    c_lastUpdateDateTim
)