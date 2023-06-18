USE shop;

INSERT INTO roles (name, permissions)
VALUES ('ROLE_USER', 'READ:USER,READ:ITEM'),
       ('ROLE_ADMIN', 'READ:USER,READ:ADMIN,CREATE:ADMIN,READ:ITEM,CREATE:ITEM,UPDATE:ITEM,DELETE:ITEM');

INSERT INTO users (created_on, role_id, email, first_name, last_name, password)
VALUES (CURRENT_TIMESTAMP, 2, 'admin@gmail.com', 'root', 'admin', '$2a$12$/0gAnWN20uNtXlEz8R2
.Eecf8kBKu7KtUve7brSczg8fOmbJPdMT2');

INSERT INTO items (is_half_price, price, name)
VALUES (FALSE, 50, 'apple'),
       (FALSE, 40, 'banana'),
       (FALSE, 30, 'tomato'),
       (TRUE,26, 'potato');