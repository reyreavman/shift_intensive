create type transferStatus as enum ('SUCCESSFUL', 'FAILED');

create table if not exists wallet.transfers (
    id                  serial primary key,
    senderId          int not null references wallet.users(id),
    recipientId       int not null references wallet.users(id),
    amount            int not null,
    status            transferStatus not null,
    dateTime          timestamp not null default current_timestamp
);