create table if not exists wallet.wallets (
    id                int primary key references wallet.users(id),
    c_balance         int not null default 100
)