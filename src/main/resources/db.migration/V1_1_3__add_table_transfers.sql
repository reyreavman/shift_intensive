create table if not exists wallet.transfers (
    id                  serial primary key,
    sender_id           int not null references wallet.users(id),
    recipient_id        int not null references wallet.users(id),
    amount              int not null,
    status              varchar not null,
    creation_date_time    timestamp not null
);