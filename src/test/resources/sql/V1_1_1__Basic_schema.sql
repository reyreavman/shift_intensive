create schema if not exists wallet;

drop table if exists wallet.users;

create table if not exists wallet.users (
    id                            serial primary key,
    first_name                    varchar(50) not null,
    middle_name                   varchar(50),
    last_name                     varchar(50) not null,
    phone_number                  varchar(11) not null unique,
    email                         varchar(255) not null unique,
    birthdate                     date not null,
    password_hash                 varchar(255) not null,
    creation_time_stamp           timestamp,
    last_update_time_stamp        timestamp
);

insert into wallet.users (first_name, middle_name, last_name, phone_number, email, birthdate, password_hash) values
    ('Иван', 'Иванович', 'Петров', '79123456789', 'ivanov@example.com', '1980-01-01', 'hashed_password'),
    ('Мария', 'Петровна', 'Сидорова', '79876543210', 'sidoorova@example.com', '1990-02-02', 'another_hashed_password'),
    ('Александр', 'Васильевич', 'Кузнецов', '79032145678', 'kuznetsov@example.com', '2000-03-03', 'third_hashed_password'),
    ('Екатерина', 'Дмитриевна', 'Попова', '79243256789', 'popova@example.com', '2010-04-04', 'fourth_hashed_password'),
    ('Дмитрий', 'Сергеевич', 'Иванов', '79456372891', 'divanov@example.com', '2020-05-05', 'fifth_hashed_password');


create table if not exists wallet.wallets (
    user_id           int primary key references wallet.users(id),
    balance           int not null
);

insert into wallet.wallets (user_id, balance) values
    (1, 100),
    (2, 100),
    (3, 100),
    (4, 100),
    (5, 100);


create table if not exists wallet.transfers (
    id                  serial primary key,
    sender_id           int not null references wallet.users(id),
    recipient_id        int not null references wallet.users(id),
    amount              int not null,
    status              varchar not null,
    creation_date_time  timestamp
);

insert into wallet.transfers (sender_id, recipient_id, amount, status) values
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