INSERT INTO users (id, user_name, password, email, first_name, last_name)
VALUES (1, 'admin', 'Adminpassword123', 'admin@example.com', 'Admin', 'Administrator');

INSERT INTO users (id, user_name, password, email, first_name, last_name)
VALUES (2, 'asmith', 'securePass!', 'asmith@example.com', 'Alice', 'Smith');

INSERT INTO users (id, user_name, password, email, first_name, last_name)
VALUES (3, 'bwilliams', 'myPass#2024', 'bwilliams@example.com', 'Bob', 'Williams');

INSERT INTO roles (id, role_name)
VALUES (1, 'USER');

INSERT INTO roles (id, role_name)
VALUES (2, 'ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);

INSERT INTO users_roles (user_id, role_id)
VALUES (2, 1);

INSERT INTO users_roles (user_id, role_id)
VALUES (3, 1);
