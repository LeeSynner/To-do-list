INSERT INTO users(username, password, role)
VALUES
    ('user', '$2a$10$D5qUXlxrFp2bhH1Yg2xJE.dWsuC.AtY0Tp6rsW/04aLvH/xh/8DPK', 'ROLE_ADMIN');

INSERT INTO tasks(title, description, due_date, is_completed, user_id, created_at, updated_at)
VALUES
    ('Finish project report', 'Complete the final report for the project', '2025-05-10 17:00:00', FALSE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Buy groceries', 'Purchase milk, bread, and eggs', '2025-05-06 15:00:00', FALSE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Doctor appointment', 'Annual check-up at the clinic', '2025-05-08 09:00:00', FALSE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Meeting with the team', 'Discuss the
     progress of the current sprint', '2025-05-07 11:00:00', TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);