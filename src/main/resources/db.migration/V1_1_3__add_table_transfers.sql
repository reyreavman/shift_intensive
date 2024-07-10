create table if not exists wallet.transfers (
    id                  serial primary key,
    senderWalletId      int not null references wallet.users(id),
    recipientWalletId   int not null references wallet.users(id),
    amount              int not null,
    status              varchar not null,
    creationDateTime    timestamp not null
);