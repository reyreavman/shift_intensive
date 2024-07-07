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

insert into wallet.users (c_first_name, c_middle_name, c_last_name, c_phone_number, c_email, c_birthdate, c_password_hash) values
    ('Иван', 'Иванович', 'Петров', '79123456789', 'ivanov@example.com', '1980-01-01', 'hashed_password'),
    ('Мария', 'Петровна', 'Сидорова', '79876543210', 'sidoorova@example.com', '1990-02-02', 'another_hashed_password'),
    ('Александр', 'Васильевич', 'Кузнецов', '79032145678', 'kuznetsov@example.com', '2000-03-03', 'third_hashed_password'),
    ('Екатерина', 'Дмитриевна', 'Попова', '79243256789', 'popova@example.com', '2010-04-04', 'fourth_hashed_password'),
    ('Дмитрий', 'Сергеевич', 'Иванов', '79456372891', 'divanov@example.com', '2020-05-05', 'fifth_hashed_password');


create table if not exists wallet.wallets (
    user_id                int primary key references wallet.users(id),
    c_balance         int not null default 100
);

insert into wallet.wallets (user_id) values
    (1),
    (2),
    (3),
    (4),
    (5);


create table if not exists wallet.transfers_among_users (
    id                  serial primary key,
    c_sender_id          int not null references wallet.users(id),
    c_recipient_id       int not null references wallet.users(id),
    c_amount            int not null check (c_amount > 0),
    c_status            varchar not null,
    c_date_time          timestamp default null
);

insert into wallet.transfers_among_users (c_sender_id, c_recipient_id, c_amount, c_status) values
    (1, 2, 50, 'SUCCESSFUL'),
    (3, 4, 20, 'FAILED'),
    (2, 1, 75, 'SUCCESSFUL'),
    (4, 5, 10, 'SUCCESSFUL'),
    (5, 1, 30, 'FAILED'),
    (3, 2, 60, 'SUCCESSFUL'),
    (1, 4, 90, 'SUCCESSFUL'),
    (5, 3, 15, 'FAILED'),
    (2, 5, 85, 'SUCCESSFUL'),
    (2, 5, 85, 'SUCCESSFUL'),
    (4, 1, 25, 'SUCCESSFUL');