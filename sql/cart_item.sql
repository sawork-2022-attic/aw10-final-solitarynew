create table cart_item
(
    id         int not null
        primary key,
    amount     int null,
    product_id int null,
    cart_id    int null
);

INSERT INTO test.cart_item (id, amount, product_id, cart_id) VALUES (2, 90, 2, 319);
INSERT INTO test.cart_item (id, amount, product_id, cart_id) VALUES (20, 90, 2, 3190);
INSERT INTO test.cart_item (id, amount, product_id, cart_id) VALUES (46, 2, 1, 319);
INSERT INTO test.cart_item (id, amount, product_id, cart_id) VALUES (47, 2, 1, 319);
INSERT INTO test.cart_item (id, amount, product_id, cart_id) VALUES (416, 2, 1, 319);
INSERT INTO test.cart_item (id, amount, product_id, cart_id) VALUES (460, 2, 1, 3190);
INSERT INTO test.cart_item (id, amount, product_id, cart_id) VALUES (470, 2, 1, 3190);
