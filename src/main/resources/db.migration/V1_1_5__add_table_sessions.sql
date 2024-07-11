create table if not exists wallet.sessions (
    token           varchar primary key,
    user_id         bigint references users (id),
    active          boolean not null,
    expiration_time timestamp
);