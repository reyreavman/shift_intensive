create table if not exists wallet.wallets (
    id              int primary key references wallet.users(id),
    balance         int not null
)