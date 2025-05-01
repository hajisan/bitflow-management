-- Indsæt brugere
INSERT INTO User (firstName, lastName, email, password, role) VALUES
('Admin', 'Admin', 'admin@bitbros.dk', 'admin', 'ADMIN'), -- ID 1
('Projektleder', 'BitBro', 'projektleder@bitbros.dk', 'projektleder', 'PROJECT_MANAGER'), -- ID 2
('Sofie', 'Rytter', 'sofie@bitbros.dk', 'sofie', 'DEVELOPER'), -- ID 3
('Joakim', 'Serverinsen', 'joakim@bitbros.dk', 'joakim', 'DEVELOPER'), -- ID 4
('Nima', 'Salami', 'nima@bitbros.dk', 'nima', 'DEVELOPER'); -- ID 5

-- Indsæt projekt
INSERT INTO Project (name, description, deadline, estimatedTime, timeSpent, status) VALUES 
('Projekt Alpha', 'Et eksempelprojekt', '2025-05-31', 160, 0, 'ACTIVE');

-- Indsæt subprojekter
INSERT INTO Subproject (projectID, name, description, deadline, estimatedTime, timeSpent, status) VALUES
(1000, 'Backend', 'Delprojekt: Backend', '2025-05-31', 80, 0, 'ACTIVE'),
(1000, 'Frontend', 'Delprojekt: Frontend', '2025-05-31', 80, 0, 'ACTIVE');

-- Indsæt tasks
INSERT INTO Task (subProjectID, name, description, deadline, estimatedTime, status) VALUES
(10000, 'Backend_Task_1', 'Opgave 1', '2025-05-31', 10, 'ACTIVE'),
(10000, 'Backend_Task_2', 'Opgave 2', '2025-05-31', 10, 'ACTIVE'),
(10000, 'Backend_Task_3', 'Opgave 3', '2025-05-31', 10, 'ACTIVE'),
(10000, 'Backend_Task_4', 'Opgave 4', '2025-05-31', 10, 'ACTIVE'),
(10001, 'Frontend_Task_1', 'Opgave 1', '2025-05-31', 10, 'ACTIVE'),
(10001, 'Frontend_Task_2', 'Opgave 2', '2025-05-31', 10, 'ACTIVE'),
(10001, 'Frontend_Task_3', 'Opgave 3', '2025-05-31', 10, 'ACTIVE'),
(10001, 'Frontend_Task_4', 'Opgave 4', '2025-05-31', 10, 'ACTIVE');

-- Indsæt subtasks
INSERT INTO SubTask (taskID, name, description, deadline, estimatedTime, status) VALUES
(100000, 'Task_1_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
(100000, 'Task_1_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
(100001, 'Task_2_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
(100001, 'Task_2_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
(100002, 'Task_3_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
(100002, 'Task_3_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
(100003, 'Task_4_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
(100003, 'Task_4_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
(100004, 'Task_5_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
(100004, 'Task_5_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
(100005, 'Task_6_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
(100005, 'Task_6_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
(100006, 'Task_7_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
(100006, 'Task_7_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
(100007, 'Task_8_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
(100007, 'Task_8_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE');

-- Indsæt time entries
INSERT INTO TimeEntry (date, hoursSpent) VALUES
('2025-05-01', 2),
('2025-05-01', 2),
('2025-05-01', 4),
('2025-05-01', 2),
('2025-05-01', 2),
('2025-05-01', 4),
('2025-05-01', 2),
('2025-05-01', 2),
('2025-05-01', 4),
('2025-05-01', 2),
('2025-05-01', 2),
('2025-05-01', 4),
('2025-05-01', 2),
('2025-05-01', 2),
('2025-05-01', 4),
('2025-05-01', 2),
('2025-05-01', 2),
('2025-05-01', 4),
('2025-05-01', 2),
('2025-05-01', 2),
('2025-05-01', 4),
('2025-05-01', 2),
('2025-05-01', 2),
('2025-05-01', 4);

-- Knyt time entries til tasks
INSERT INTO TimeEntry_Task (timeEntryID, taskID) VALUES
(10000002, 100000),
(10000005, 100001),
(10000008, 100002),
(10000011, 100003),
(10000014, 100004),
(10000017, 100005),
(10000020, 100006),
(10000023, 100007);

-- Knyt time entries til subtasks
INSERT INTO TimeEntry_SubTask (timeEntryID, subTaskID) VALUES
(10000000, 1000000),
(10000001, 1000001),
(10000002, 1000002),
(10000003, 1000003),
(10000004, 1000004),
(10000005, 1000005),
(10000006, 1000006),
(10000007, 1000007),
(10000008, 1000008),
(10000009, 1000009),
(10000010, 1000010),
(10000011, 1000011),
(10000012, 1000012),
(10000013, 1000013),
(10000014, 1000014),
(10000015, 1000015);

-- Knyt projektleder til projekt
INSERT INTO User_Project (userID, projectID) VALUES (2, 1000); -- Projektleder ID 1

-- Knyt udviklere til subprojekter
INSERT INTO User_Subproject (userID, subProjectID) VALUES (3, 10000), (4, 10001);  -- Sofie ID 3 & Joakim ID 4

-- Knyt udviklere til tasks
INSERT INTO User_Task (userID, taskID) VALUES
(3, 100000), -- Sofie ID 3
(3, 100001), -- Sofie ID 3
(3, 100002), -- Sofie ID 3
(4, 100003), -- Joakim ID 4
(4, 100004), -- Joakim ID 4
(4, 100005), -- Joakim ID 4
(5, 100006), -- Nima ID 5
(5, 100007); -- Nima ID 5

-- Knyt udviklere til subtasks
INSERT INTO User_SubTask (userID, subTaskID) VALUES 
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
