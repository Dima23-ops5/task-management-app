INSERT INTO users (id, user_name, password, email, first_name, last_name)
VALUES (1, 'bobUser', 'bobPassword12345', 'bobemail@gmail.com', 'Bob', 'Peterson');

INSERT INTO users (id, user_name, password, email, first_name, last_name)
VALUES (2, 'Alice', 'AlicePassword12345', 'Aliceemail@gmail.com', 'Alice', 'Bennett');

INSERT INTO roles (id, role_name)
VALUES (1, 'USER');

INSERT INTO roles (id, role_name)
VALUES (2, 'ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);

INSERT INTO users_roles (user_id, role_id)
VALUES (2, 1);

INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (1, 'Car sharing', 'Service that can help people rent cars', '2024-11-01', '2024-11-05', 'IN_PROGRESS');

INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (2, 'Python', 'simple game', '2024-11-03', '2024-11-07', 'INITIATED');

INSERT INTO projects (id, name, description, start_date, end_date, status)
VALUES (3, 'To do', 'create tasks that need to do', '2024-11-02', '2024-11-08', 'INITIATED');

INSERT INTO user_project (project_id, user_id)
VALUES (1, 1);

INSERT INTO user_project (project_id, user_id)
VALUES (2, 1);

INSERT INTO user_project (project_id, user_id)
VALUES (2, 2);

INSERT INTO user_project (project_id, user_id)
VALUES (3, 2);
