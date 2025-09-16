create table if not exists "user" (
    id bigint primary key,
    email varchar(255) not null unique,
    password varchar(255) not null,
    first_name varchar(100),
    last_name varchar(100),
    enabled boolean not null default true,
    created_at timestamp without time zone default now()
);

create table if not exists user_roles (
    user_id bigint not null references users(id) on delete cascade,
    "role" varchar(32) not null,
    primary key (user_id, "role")
);

create index if not exists idx_user_email on users(email);