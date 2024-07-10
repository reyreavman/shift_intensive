create table if not exists wallet.wallets (
    user_id     int primary key references wallet.users(id),
    balance     int not null
)