INSERT INTO users (id, user_name, password, email, first_name, last_name)
VALUES (1, 'admin', 'Adminpassword123', 'admin@example.com', 'Admin', 'Administrator');

INSERT INTO users (id, user_name, password, email, first_name, last_name)
VALUES (2, 'asmith', 'securePass!', 'asmith@example.com', 'Alice', 'Smith');

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

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (1, 'Develop Game Logic', 'Implement the core game mechanics and rules', 'HIGH', 'IN_PROCESS', '2024-11-05', 2, 1);

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (2, 'Design Game UI', 'Create a simple user interface for the game', 'MEDIUM', 'NOT_STARTED', '2024-11-06', 2, 2);

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (3, 'Testing and Bug Fixing', 'Test the app and resolve any bugs', 'HIGH', 'IN_PROCESS', '2024-11-07', 3, 2);

INSERT INTO tasks (id, name, description, priority, status, due_date, project_id, assignee_id)
VALUES (4, 'Design User Profile Page', 'Create a responsive and user-friendly profile page design.', 'MEDIUM', 'IN_PROCESS', '2024-12-08', 1, 1);

INSERT INTO user_project (project_id, user_id)
VALUES (1, 1);

INSERT INTO user_project (project_id, user_id)
VALUES (2, 1);

INSERT INTO user_project (project_id, user_id)
VALUES (2, 2);
