create table delivery
(
    id       int          not null
        primary key,
    order_id int          null,
    status   varchar(200) null
);

INSERT INTO test.delivery (id, order_id, status) VALUES (3190, 3190, 'CREATED');
