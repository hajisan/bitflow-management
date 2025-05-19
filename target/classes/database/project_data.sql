-- Indsæt brugere
INSERT INTO Users (firstName, lastName, email, passwordHash, role) VALUES
('Admin', 'Admin', 'admin@bitbros.dk', '$2a$12$MGilL/UlYqHFlxUQPN472e1pc9no6/v.V1euob3c3R0kjqUYDL5Ua', 'ADMIN'), -- ID 1 - passwordHash 'admin'
('Projektleder', 'BitBro', 'projektleder@bitbros.dk', '$2a$12$XerhDcoG6tIwL.7giuUxg.W4K3zP2UMt7sqhb/ghoXwReQHj8lbD.', 'PROJECT_MANAGER'), -- ID 2 - passwordHash 'projektleder'
('Sofie', 'Rytter', 'sofie@bitbros.dk', '$2a$12$Aswqt9sTSdfydqIhoyNEgesHjWKp0aqS2Ku9WoaVskL1/z0dLtVZ6', 'DEVELOPER'), -- ID 3 - passwordHash 'sofie'
('Joakim', 'Severinsen', 'joakim@bitbros.dk', '$2a$12$04TS8rlFidB7O4PGEGgn2uKVY6TtVn1X6ngk/wPgQ/zrrvu84cwG.', 'DEVELOPER'), -- ID 4 - passwordHash 'joakim'
('Nima', 'Salami', 'nima@bitbros.dk', '$2a$12$lLteHXSInISgzrHlMzCuEeN02o4TeKXjsFHrweUoGfqsvU1OFzKwm', 'DEVELOPER'); -- ID 5 - passwordHash 'nima'

-- Indsæt projekt
INSERT INTO Project (name, description, deadline, estimatedTime, timeSpent, status) VALUES 
('Projekt Alpha', 'Et eksempelprojekt', '2025-05-31', 160, 0, 'ACTIVE');

-- Indsæt subprojekter
INSERT INTO Subproject (projectID, name, description, deadline, estimatedTime, timeSpent, status) VALUES
(1000, 'Backend', 'Delprojekt: Backend', '2025-05-31', 80, 0, 'ACTIVE'),
(1000, 'Frontend', 'Delprojekt: Frontend', '2025-05-31', 80, 0, 'ACTIVE');

-- Indsæt tasks
INSERT INTO Task (subProjectID, name, description, deadline, estimatedTime, timeSpent, status) VALUES
(10000, 'Backend_Task_1', 'Opgave 1', '2025-05-31', 10, 0, 'ACTIVE'),
(10000, 'Backend_Task_2', 'Opgave 2', '2025-05-31', 10, 0, 'ACTIVE'),
(10000, 'Backend_Task_3', 'Opgave 3', '2025-05-31', 10, 0, 'ACTIVE'),
(10000, 'Backend_Task_4', 'Opgave 4', '2025-05-31', 10, 0, 'ACTIVE'),
(10001, 'Frontend_Task_1', 'Opgave 1', '2025-05-31', 10, 0, 'ACTIVE'),
(10001, 'Frontend_Task_2', 'Opgave 2', '2025-05-31', 10, 0, 'ACTIVE'),
(10001, 'Frontend_Task_3', 'Opgave 3', '2025-05-31', 10, 0, 'ACTIVE'),
(10001, 'Frontend_Task_4', 'Opgave 4', '2025-05-31', 10, 0, 'ACTIVE');

-- Indsæt subtasks
INSERT INTO SubTask (taskID, name, description, deadline, estimatedTime, timeSpent, status) VALUES
(100000, 'Task_1_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
(100000, 'Task_1_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
(100001, 'Task_2_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
(100001, 'Task_2_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
(100002, 'Task_3_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
(100002, 'Task_3_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
(100003, 'Task_4_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
(100003, 'Task_4_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0,'ACTIVE'),
(100004, 'Task_5_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
(100004, 'Task_5_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
(100005, 'Task_6_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
(100005, 'Task_6_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
(100006, 'Task_7_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
(100006, 'Task_7_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
(100007, 'Task_8_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
(100007, 'Task_8_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE');

-- Knyt user til projekt
INSERT INTO Users_Project (userID, projectID) VALUES
(2, 1000), -- Projektleder ID 1
(3, 1000), -- Developer Sofie ID 3
(4, 1000); -- Developer Joakim ID 4

-- Knyt udviklere til subprojekter
INSERT INTO Users_Subproject (userID, subProjectID) VALUES (3, 10000), (4, 10001);  -- Sofie ID 3 & Joakim ID 4

-- Knyt udviklere til tasks
INSERT INTO Users_Task (userID, taskID) VALUES
(3, 100000), -- Sofie ID 3
(3, 100001), -- Sofie ID 3
(3, 100002), -- Sofie ID 3
(4, 100003), -- Joakim ID 4
(4, 100004), -- Joakim ID 4
(4, 100005), -- Joakim ID 4
(5, 100006), -- Nima ID 5
(5, 100007); -- Nima ID 5

-- Knyt udviklere til subtasks
INSERT INTO Users_SubTask (userID, subTaskID) VALUES
(3, 1000000), -- Sofie ID 3
(3, 1000001), -- Sofie ID 3
(3, 1000002), -- Sofie ID 3
(3, 1000003), -- Sofie ID 3
(3, 1000004), -- Sofie ID 3
(4, 1000005), -- Joakim ID 4
(4, 1000006), -- Joakim ID 4
(4, 1000007), -- Joakim ID 4
(4, 1000008), -- Joakim ID 4
(4, 1000009), -- Joakim ID 4
(5, 1000010), -- Nima ID 5
(5, 1000011), -- Nima ID 5
(5, 1000012), -- Nima ID 5
(5, 1000013), -- Nima ID 5
(5, 1000014), -- Nima ID 5
(5, 1000015); -- Nima ID 5
