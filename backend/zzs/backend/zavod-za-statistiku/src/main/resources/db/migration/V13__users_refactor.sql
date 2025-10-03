DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id bigserial primary key,
    email varchar(255) not null unique,
    first_name varchar(255),
    last_name varchar(255),
    date_of_birth DATE NOT NULL,
    city TEXT NOT NULL,
    address TEXT NOT NULL,
    region VARCHAR(32) NOT NULL,
    jmbg VARCHAR(20) NOT NULL UNIQUE,
    gender VARCHAR(200) NOT NULL,
    created_at timestamp without time zone default now(),
    "role" VARCHAR(32) NOT NULL,
    auth0_user_id TEXT UNIQUE
)