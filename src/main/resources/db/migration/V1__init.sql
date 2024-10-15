-- Create sequences
CREATE SEQUENCE IF NOT EXISTS orders_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS products_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS ratings_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS transactions_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

-- Create tables
CREATE TABLE IF NOT EXISTS orders (
    status SMALLINT NOT NULL CHECK (status BETWEEN 0 AND 5),
    created_at TIMESTAMP(6),
    id BIGINT NOT NULL,
    owner_id BIGINT,
    transaction_id BIGINT UNIQUE,
    updated_at TIMESTAMP(6),
    order_id VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS products (
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

CREATE TABLE IF NOT EXISTS ratings (
    rating INTEGER,
    created_at TIMESTAMP(6),
    id BIGINT NOT NULL,
    product_id BIGINT,
    updated_at TIMESTAMP(6),
    user_id BIGINT,
    rating_id VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS transactions (
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

CREATE TABLE IF NOT EXISTS users (
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
