create sequence orders_seq start with 1 increment by 50;
create sequence products_seq start with 1 increment by 50;
create sequence ratings_seq start with 1 increment by 50;
create sequence transactions_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table order_item (id bigint generated by default as identity, order_id bigint, product_id bigint, quantity bigint, primary key (id));
create table orders (status smallint not null check (status between 0 and 5), created_at timestamp(6), id bigint not null, owner_id bigint, transaction_id bigint unique, updated_at timestamp(6), order_id varchar(255) not null unique, primary key (id));
create table products (price float(53) not null, created_at timestamp(6), id bigint not null, quantity bigint not null, seller_id bigint, updated_at timestamp(6), description varchar(255) not null, product_id varchar(255) not null unique, title varchar(255) not null, primary key (id));
create table ratings (rating smallint not null check (rating between 0 and 4), created_at timestamp(6), id bigint not null, product_id bigint, updated_at timestamp(6), user_id bigint, rating_id varchar(255) not null unique, primary key (id));
create table transactions (amount float(53) not null, status smallint not null check (status between 0 and 2), created_at timestamp(6), id bigint not null, timestamp timestamp(6), updated_at timestamp(6), payment_method varchar(255) not null, transaction_id varchar(255) not null unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), email varchar(255) not null unique, phone_number varchar(255), sub varchar(255) not null unique, primary key (id));
alter table if exists order_item add constraint FKt4dc2r9nbvbujrljv3e23iibt foreign key (order_id) references orders;
alter table if exists order_item add constraint FKc5uhmwioq5kscilyuchp4w49o foreign key (product_id) references products;
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
create table order_item (id bigint generated by default as identity, order_id bigint, product_id bigint, quantity bigint, primary key (id));
create table orders (status smallint not null check (status between 0 and 5), created_at timestamp(6), id bigint not null, owner_id bigint, transaction_id bigint unique, updated_at timestamp(6), order_id varchar(255) not null unique, primary key (id));
create table products (price float(53) not null, created_at timestamp(6), id bigint not null, quantity bigint not null, seller_id bigint, updated_at timestamp(6), description varchar(255) not null, product_id varchar(255) not null unique, title varchar(255) not null, primary key (id));
create table ratings (rating smallint not null check (rating between 0 and 4), created_at timestamp(6), id bigint not null, product_id bigint, updated_at timestamp(6), user_id bigint, rating_id varchar(255) not null unique, primary key (id));
create table transactions (amount float(53) not null, status smallint not null check (status between 0 and 2), created_at timestamp(6), id bigint not null, timestamp timestamp(6), updated_at timestamp(6), payment_method varchar(255) not null, transaction_id varchar(255) not null unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), email varchar(255) not null unique, phone_number varchar(255), sub varchar(255) not null unique, primary key (id));
alter table if exists order_item add constraint FKt4dc2r9nbvbujrljv3e23iibt foreign key (order_id) references orders;
alter table if exists order_item add constraint FKc5uhmwioq5kscilyuchp4w49o foreign key (product_id) references products;
alter table if exists orders add constraint FKml4dss6fai1c8icav9hoq47o8 foreign key (owner_id) references users;
alter table if exists orders add constraint FK61jxrgf6m4706ex3tyrilmtat foreign key (transaction_id) references transactions;
alter table if exists products add constraint FKbgw3lyxhsml3kfqnfr45o0vbj foreign key (seller_id) references users;
alter table if exists ratings add constraint FK228us4dg38ewge41gos8y761r foreign key (product_id) references products;
alter table if exists ratings add constraint FKb3354ee2xxvdrbyq9f42jdayd foreign key (user_id) references users;
