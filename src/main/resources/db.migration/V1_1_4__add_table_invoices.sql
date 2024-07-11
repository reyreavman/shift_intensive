create extension if not exists "uuid-ossp";

create table if not exists wallet.invoices (
    id                  uuid primary key default uuid_generate_v4(),
    sender_id           int not null references wallet.users(id),
    recipient_id        int not null references wallet.users(id),
    amount              int not null,
    transfer_id         int references wallet.transfers(id),
    comment             varchar(250),
    status              varchar not null,
    creation_date_time  timestamp not null
 );