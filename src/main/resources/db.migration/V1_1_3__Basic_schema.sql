create type transferStatus as enum ('SUCCESSFUL', 'FAILED');

create table if not exists wallet.transfers_among_users (
    id                  serial primary key,
    c_senderId          int not null references wallet.users(id),
    c_recipientId       int not null references wallet.users(id),
    c_amount            int not null check (c_amount > 0),
    c_status            transferStatus not null,
    c_dateTime          timestamp not null default current_timestamp
);