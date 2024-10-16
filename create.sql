-- Create sequences
CREATE SEQUENCE orders_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE products_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE ratings_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE transactions_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 50;

-- Create tables
CREATE TABLE orders (
    status SMALLINT NOT NULL CHECK (status BETWEEN 0 AND 5),
    created_at TIMESTAMP(6),
    id BIGINT NOT NULL,
    owner_id BIGINT,
    transaction_id BIGINT UNIQUE,
    updated_at TIMESTAMP(6),
    order_id VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE products (
    price FLOAT(53) NOT NULL,
    created_at TIMESTAMP(6),
    id BIGINT NOT NULL,
    seller_id BIGINT,
    updated_at TIMESTAMP(6),
    description VARCHAR(255) NOT NULL,
    product_id VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE ratings (
    rating INTEGER,
    created_at TIMESTAMP(6),
    id BIGINT NOT NULL,
    product_id BIGINT,
    updated_at TIMESTAMP(6),
    user_id BIGINT,
    rating_id VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE transactions (
    amount FLOAT(53) NOT NULL,
    status SMALLINT NOT NULL CHECK (status BETWEEN 0 AND 2),
    created_at TIMESTAMP(6),
    id BIGINT NOT NULL,
    timestamp TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    payment_method VARCHAR(255) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE users (
    created_at TIMESTAMP(6),
    id BIGINT NOT NULL,
    updated_at TIMESTAMP(6),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255),
    sud VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

-- Add foreign key constraints
ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT FKml4dss6fai1c8icav9hoq47o8 FOREIGN KEY (owner_id) REFERENCES users;

ALTER TABLE IF EXISTS orders
    ADD CONSTRAINT FK61jxrgf6m4706ex3tyrilmtat FOREIGN KEY (transaction_id) REFERENCES transactions;

ALTER TABLE IF EXISTS products
    ADD CONSTRAINT FKbgw3lyxhsml3kfqnfr45o0vbj FOREIGN KEY (seller_id) REFERENCES users;

ALTER TABLE IF EXISTS ratings
    ADD CONSTRAINT FK228us4dg38ewge41gos8y761r FOREIGN KEY (product_id) REFERENCES products;

ALTER TABLE IF EXISTS ratings
    ADD CONSTRAINT FKb3354ee2xxvdrbyq9f42jdayd FOREIGN KEY (user_id) REFERENCES users;
create sequence orders_seq start with 1 increment by 50;
create sequence products_seq start with 1 increment by 50;
create sequence ratings_seq start with 1 increment by 50;
create sequence transactions_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table orders (status smallint not null check (status between 0 and 5), created_at timestamp(6), id bigint not null, owner_id bigint, transaction_id bigint unique, updated_at timestamp(6), order_id varchar(255) not null unique, primary key (id));
create table products (price float(53) not null, created_at timestamp(6), id bigint not null, seller_id bigint, updated_at timestamp(6), description varchar(255) not null, product_id varchar(255) not null unique, title varchar(255) not null, primary key (id));
create table ratings (rating integer, created_at timestamp(6), id bigint not null, product_id bigint, updated_at timestamp(6), user_id bigint, rating_id varchar(255) not null unique, primary key (id));
create table transactions (amount float(53) not null, status smallint not null check (status between 0 and 2), created_at timestamp(6), id bigint not null, timestamp timestamp(6), updated_at timestamp(6), payment_method varchar(255) not null, transaction_id varchar(255) not null unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), email varchar(255) not null unique, phone_number varchar(255), sud varchar(255) not null unique, primary key (id));
alter table if exists orders add constraint FKml4dss6fai1c8icav9hoq47o8 foreign key (owner_id) references users;
alter table if exists orders add constraint FK61jxrgf6m4706ex3tyrilmtat foreign key (transaction_id) references transactions;
alter table if exists products add constraint FKbgw3lyxhsml3kfqnfr45o0vbj foreign key (seller_id) references users;
alter table if exists ratings add constraint FK228us4dg38ewge41gos8y761r foreign key (product_id) references products;
alter table if exists ratings add constraint FKb3354ee2xxvdrbyq9f42jdayd foreign key (user_id) references users;
create sequence orders_seq start with 1 increment by 50;
create sequence products_seq start with 1 increment by 50;
create sequence ratings_seq start with 1 increment by 50;
create sequence transactions_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table orders (status smallint not null check (status between 0 and 5), created_at timestamp(6), id bigint not null, owner_id bigint, transaction_id bigint unique, updated_at timestamp(6), order_id varchar(255) not null unique, primary key (id));
create table products (price float(53) not null, created_at timestamp(6), id bigint not null, seller_id bigint, updated_at timestamp(6), description varchar(255) not null, product_id varchar(255) not null unique, title varchar(255) not null, primary key (id));
create table ratings (rating integer, created_at timestamp(6), id bigint not null, product_id bigint, updated_at timestamp(6), user_id bigint, rating_id varchar(255) not null unique, primary key (id));
create table transactions (amount float(53) not null, status smallint not null check (status between 0 and 2), created_at timestamp(6), id bigint not null, timestamp timestamp(6), updated_at timestamp(6), payment_method varchar(255) not null, transaction_id varchar(255) not null unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), email varchar(255) not null unique, phone_number varchar(255), sud varchar(255) not null unique, primary key (id));
alter table if exists orders add constraint FKml4dss6fai1c8icav9hoq47o8 foreign key (owner_id) references users;
alter table if exists orders add constraint FK61jxrgf6m4706ex3tyrilmtat foreign key (transaction_id) references transactions;
alter table if exists products add constraint FKbgw3lyxhsml3kfqnfr45o0vbj foreign key (seller_id) references users;
alter table if exists ratings add constraint FK228us4dg38ewge41gos8y761r foreign key (product_id) references products;
alter table if exists ratings add constraint FKb3354ee2xxvdrbyq9f42jdayd foreign key (user_id) references users;
create sequence orders_seq start with 1 increment by 50;
create sequence products_seq start with 1 increment by 50;
create sequence ratings_seq start with 1 increment by 50;
create sequence transactions_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table orders (status smallint not null check (status between 0 and 5), created_at timestamp(6), id bigint not null, owner_id bigint, transaction_id bigint unique, updated_at timestamp(6), order_id varchar(255) not null unique, primary key (id));
create table products (price float(53) not null, created_at timestamp(6), id bigint not null, seller_id bigint, updated_at timestamp(6), description varchar(255) not null, product_id varchar(255) not null unique, title varchar(255) not null, primary key (id));
create table ratings (rating integer, created_at timestamp(6), id bigint not null, product_id bigint, updated_at timestamp(6), user_id bigint, rating_id varchar(255) not null unique, primary key (id));
create table transactions (amount float(53) not null, status smallint not null check (status between 0 and 2), created_at timestamp(6), id bigint not null, timestamp timestamp(6), updated_at timestamp(6), payment_method varchar(255) not null, transaction_id varchar(255) not null unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), email varchar(255) not null unique, phone_number varchar(255), sud varchar(255) not null unique, primary key (id));
alter table if exists orders add constraint FKml4dss6fai1c8icav9hoq47o8 foreign key (owner_id) references users;
alter table if exists orders add constraint FK61jxrgf6m4706ex3tyrilmtat foreign key (transaction_id) references transactions;
alter table if exists products add constraint FKbgw3lyxhsml3kfqnfr45o0vbj foreign key (seller_id) references users;
alter table if exists ratings add constraint FK228us4dg38ewge41gos8y761r foreign key (product_id) references products;
alter table if exists ratings add constraint FKb3354ee2xxvdrbyq9f42jdayd foreign key (user_id) references users;
create sequence orders_seq start with 1 increment by 50;
create sequence products_seq start with 1 increment by 50;
create sequence ratings_seq start with 1 increment by 50;
create sequence transactions_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table orders (status smallint not null check (status between 0 and 5), created_at timestamp(6), id bigint not null, owner_id bigint, transaction_id bigint unique, updated_at timestamp(6), order_id varchar(255) not null unique, primary key (id));
create table products (price float(53) not null, created_at timestamp(6), id bigint not null, seller_id bigint, updated_at timestamp(6), description varchar(255) not null, product_id varchar(255) not null unique, title varchar(255) not null, primary key (id));
create table ratings (rating integer, created_at timestamp(6), id bigint not null, product_id bigint, updated_at timestamp(6), user_id bigint, rating_id varchar(255) not null unique, primary key (id));
create table transactions (amount float(53) not null, status smallint not null check (status between 0 and 2), created_at timestamp(6), id bigint not null, timestamp timestamp(6), updated_at timestamp(6), payment_method varchar(255) not null, transaction_id varchar(255) not null unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), email varchar(255) not null unique, phone_number varchar(255), sud varchar(255) not null unique, primary key (id));
alter table if exists orders add constraint FKml4dss6fai1c8icav9hoq47o8 foreign key (owner_id) references users;
alter table if exists orders add constraint FK61jxrgf6m4706ex3tyrilmtat foreign key (transaction_id) references transactions;
alter table if exists products add constraint FKbgw3lyxhsml3kfqnfr45o0vbj foreign key (seller_id) references users;
alter table if exists ratings add constraint FK228us4dg38ewge41gos8y761r foreign key (product_id) references products;
alter table if exists ratings add constraint FKb3354ee2xxvdrbyq9f42jdayd foreign key (user_id) references users;
create sequence orders_seq start with 1 increment by 50;
create sequence products_seq start with 1 increment by 50;
create sequence ratings_seq start with 1 increment by 50;
create sequence transactions_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table orders (status smallint not null check (status between 0 and 5), created_at timestamp(6), id bigint not null, owner_id bigint, transaction_id bigint unique, updated_at timestamp(6), order_id varchar(255) not null unique, primary key (id));
create table products (price float(53) not null, created_at timestamp(6), id bigint not null, seller_id bigint, updated_at timestamp(6), description varchar(255) not null, product_id varchar(255) not null unique, title varchar(255) not null, primary key (id));
create table ratings (rating integer, created_at timestamp(6), id bigint not null, product_id bigint, updated_at timestamp(6), user_id bigint, rating_id varchar(255) not null unique, primary key (id));
create table transactions (amount float(53) not null, status smallint not null check (status between 0 and 2), created_at timestamp(6), id bigint not null, timestamp timestamp(6), updated_at timestamp(6), payment_method varchar(255) not null, transaction_id varchar(255) not null unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), email varchar(255) not null unique, phone_number varchar(255), sud varchar(255) not null unique, primary key (id));
alter table if exists orders add constraint FKml4dss6fai1c8icav9hoq47o8 foreign key (owner_id) references users;
alter table if exists orders add constraint FK61jxrgf6m4706ex3tyrilmtat foreign key (transaction_id) references transactions;
alter table if exists products add constraint FKbgw3lyxhsml3kfqnfr45o0vbj foreign key (seller_id) references users;
alter table if exists ratings add constraint FK228us4dg38ewge41gos8y761r foreign key (product_id) references products;
alter table if exists ratings add constraint FKb3354ee2xxvdrbyq9f42jdayd foreign key (user_id) references users;
create sequence orders_seq start with 1 increment by 50;
create sequence products_seq start with 1 increment by 50;
create sequence ratings_seq start with 1 increment by 50;
create sequence transactions_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table orders (status smallint not null check (status between 0 and 5), created_at timestamp(6), id bigint not null, owner_id bigint, transaction_id bigint unique, updated_at timestamp(6), order_id varchar(255) not null unique, primary key (id));
create table products (price float(53) not null, created_at timestamp(6), id bigint not null, seller_id bigint, updated_at timestamp(6), description varchar(255) not null, product_id varchar(255) not null unique, title varchar(255) not null, primary key (id));
create table ratings (rating integer, created_at timestamp(6), id bigint not null, product_id bigint, updated_at timestamp(6), user_id bigint, rating_id varchar(255) not null unique, primary key (id));
create table transactions (amount float(53) not null, status smallint not null check (status between 0 and 2), created_at timestamp(6), id bigint not null, timestamp timestamp(6), updated_at timestamp(6), payment_method varchar(255) not null, transaction_id varchar(255) not null unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), email varchar(255) not null unique, phone_number varchar(255), sud varchar(255) not null unique, primary key (id));
alter table if exists orders add constraint FKml4dss6fai1c8icav9hoq47o8 foreign key (owner_id) references users;
alter table if exists orders add constraint FK61jxrgf6m4706ex3tyrilmtat foreign key (transaction_id) references transactions;
alter table if exists products add constraint FKbgw3lyxhsml3kfqnfr45o0vbj foreign key (seller_id) references users;
alter table if exists ratings add constraint FK228us4dg38ewge41gos8y761r foreign key (product_id) references products;
alter table if exists ratings add constraint FKb3354ee2xxvdrbyq9f42jdayd foreign key (user_id) references users;
