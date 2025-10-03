DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS identity_card;
DROP TABLE IF EXISTS driving_license_categories;

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
    gender VARCHAR(50) NOT NULL,
    created_at timestamp without time zone default now(),
    "role" VARCHAR(32) NOT NULL,
    auth0_user_id TEXT UNIQUE
);

CREATE TABLE identity_cards (
    id bigserial primary key,
    name varchar(255) not null,
    surname varchar(255) not null,
    date_of_birth DATE not null,
    gender VARCHAR(50) not null,
    jmbg VARCHAR(20) not null,
    registration_number TEXT not null unique,
    date_of_issuing DATE NOT NULL,
    valid_until DATE NOT NULL,
    city TEXT NOT NULL,
    state TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(50),

    CONSTRAINT fk_identity_card_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE gun_permits (
    id bigserial primary key,
    user_id bigint not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    date_from date not null,
    date_to date not null,
    registration_number text not null,
    gun_category varchar(5) not null,
    status varchar(50),

    constraint fk_gun_permit_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE request_type (
    id bigserial primary key
);

CREATE TABLE requests (
    id bigserial PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    denial_reason TEXT,
    gun_category VARCHAR(50),
    user_id BIGINT,
    request_type_id INT,

    CONSTRAINT fk_requests_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_requests_request_type FOREIGN KEY (request_type_id) REFERENCES request_type(id)
);

CREATE TABLE vehicle_licences (
    id bigserial primary key,
    "make" varchar(255),
    model varchar(255),
    displacement int,
    manufactured_year int,
    fuel_system varchar(50),
    plate_number varchar(255),
    date_of_issuing date,
    valid_until date,
    first_name varchar(255),
    last_name varchar(255),
    address text,
    driving_license_number text not null unique,
    user_id bigint not null,
    status VARCHAR(50) not null,

    constraint fk_vehicle_licence_user FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (id) REFERENCES request_type(id)
);

CREATE TABLE driving_licenses (
    id bigserial primary key,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    date_of_birth DATE,
    date_of_issuing DATE,
    valid_until DATE,
    city VARCHAR(255),
    license_number VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(50),

    CONSTRAINT fk_driving_licenses_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE vozacka_dozvola_categories (
    driving_license_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,

    CONSTRAINT fk_license_categories FOREIGN KEY (driving_license_id) REFERENCES driving_licenses(id) ON DELETE CASCADE
);