create schema if not exists wallet;

drop table if exists wallet.users;

create table if not exists wallet.users (
    id                              serial primary key,
    c_first_name                    varchar(50) not null check (c_first_name ~* '^[А-ЯЁ][а-яё]{1,50}$'),
    c_middle_name                   varchar(50) default null check (c_middle_name ~* '^[А-ЯЁ][а-яё]{1,50}$'),
    c_last_name                     varchar(50) not null check (c_last_name ~* '^[А-ЯЁ][а-яё]{1,50}$'),
    c_phone_number                  varchar(11) not null unique check (c_phone_number ~* '^7\d{10}$'),
    c_email                         varchar(255) not null unique check (c_email ~* '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
    c_birthdate                     date not null,
    c_password_hash                 varchar(255) not null,
    c_registration_date_time        timestamp default null,
    c_last_update_date_time         timestamp default null
);

insert into wallet.users (c_first_name, c_middle_name, c_last_name, c_phone_number, c_email, c_birthdate, c_password_hash)
values
  ('Иван', 'Иванович', 'Петров', '79123456789', 'ivanov@example.com', '1980-01-01', 'hashed_password'),
  ('Мария', 'Петровна', 'Сидорова', '79876543210', 'sidoorova@example.com', '1990-02-02', 'another_hashed_password'),
  ('Александр', 'Васильевич', 'Кузнецов', '79032145678', 'kuznetsov@example.com', '2000-03-03', 'third_hashed_password'),
  ('Екатерина', 'Дмитриевна', 'Попова', '79243256789', 'popova@example.com', '2010-04-04', 'fourth_hashed_password'),
  ('Дмитрий', 'Сергеевич', 'Иванов', '79456372891', 'divanov@example.com', '2020-05-05', 'fifth_hashed_password');