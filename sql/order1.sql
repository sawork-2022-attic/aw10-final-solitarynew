create table order1
(
    id      int     not null
        primary key,
    cart_id int     null,
    total   decimal null
);

INSERT INTO test.order1 (id, cart_id, total) VALUES (3190, 3190, 7600);
