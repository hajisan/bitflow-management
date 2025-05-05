-- Opretter database, hvis den ikke allerede findes
CREATE SCHEMA IF NOT EXISTS project_db;
USE project_db;

-- Dropper tables hvis de findes for at rydde al data
DROP TABLE IF EXISTS user_subtask;
DROP TABLE IF EXISTS user_task;
DROP TABLE IF EXISTS user_subproject;
DROP TABLE IF EXISTS user_project;
DROP TABLE IF EXISTS appuser;
DROP TABLE IF EXISTS timeentry_subtask;
DROP TABLE IF EXISTS timeentry_task;
DROP TABLE IF EXISTS timeentry;
DROP TABLE IF EXISTS subtask;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS subproject;
DROP TABLE IF EXISTS project;

-- ========================
-- Tabel: AppUser
-- Gemmer information om en User
-- ========================
CREATE TABLE AppUser
(
    id        INTEGER AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(255),
    lastName  VARCHAR(255),
    email     VARCHAR(255) UNIQUE,
    password  VARCHAR(255),
    role      VARCHAR(255) CHECK (role IN ('ADMIN', 'PROJECT_MANAGER', 'DEVELOPER'))
);

-- ========================
-- Tabel: Project
-- Gemmer information om et Project
-- ========================
CREATE TABLE Project
(
    id            INTEGER AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(255) UNIQUE,
    description   VARCHAR(255),
    deadline      DATE,
    estimatedTime INTEGER,
    timeSpent     INTEGER,
    status        VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE'))
);

-- ========================
-- Tabel: subProject
-- Gemmer information om et subProject
-- ========================
CREATE TABLE Subproject
(
    id            INTEGER AUTO_INCREMENT PRIMARY KEY,
    projectID     INTEGER,
    name          VARCHAR(255),
    description   VARCHAR(255),
    deadline      DATE,
    estimatedTime INTEGER,
    timeSpent     INTEGER,
    status        VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE')),
    UNIQUE (projectID, name),
    FOREIGN KEY (projectID) REFERENCES Project (id) ON DELETE CASCADE
);

-- ========================
-- Tabel: Task
-- Gemmer information om en Task
-- ========================
CREATE TABLE Task
(
    id            INTEGER AUTO_INCREMENT PRIMARY KEY,
    subProjectID  INTEGER,
    name          VARCHAR(255),
    description   VARCHAR(255),
    deadline      DATE,
    estimatedTime INTEGER,
    status        VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE')),
    UNIQUE (subProjectID, name),
    FOREIGN KEY (subProjectID) REFERENCES Subproject (id) ON DELETE CASCADE
);

-- ========================
-- Tabel: SubTask
-- Gemmer information om en subTask
-- ========================
CREATE TABLE SubTask
(
    id            INTEGER AUTO_INCREMENT PRIMARY KEY,
    taskID        INTEGER,
    name          VARCHAR(255),
    description   VARCHAR(255),
    deadline      DATE,
    estimatedTime INTEGER,
    status        VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE')),
    UNIQUE (taskID, name),
    FOREIGN KEY (taskID) REFERENCES Task (id) ON DELETE CASCADE
);

-- ========================
-- Tabel: timeEntry
-- Gemmer information om en timeEntry
-- ========================
CREATE TABLE TimeEntry
(
    id         INTEGER AUTO_INCREMENT PRIMARY KEY,
    date       DATE,
    hoursSpent INTEGER
);

-- ========================
-- Tabel: TimeEntry_Task
-- Knytter en TimeEntry til en Task
-- ========================
CREATE TABLE TimeEntry_Task
(
    timeEntryID INTEGER,
    taskID      INTEGER,
    PRIMARY KEY (timeEntryID, taskID),
    FOREIGN KEY (timeEntryID) REFERENCES TimeEntry (id) ON DELETE CASCADE,
    FOREIGN KEY (taskID) REFERENCES Task (id) ON DELETE CASCADE
);

-- ========================
-- Tabel: TimeEntry_SubTask
-- Knytter en TimeEntry til en SubTask
-- ========================
CREATE TABLE TimeEntry_SubTask
(
    timeEntryID INTEGER,
    subTaskID   INTEGER,
    PRIMARY KEY (timeEntryID, subTaskID),
    FOREIGN KEY (timeEntryID) REFERENCES TimeEntry (id) ON DELETE CASCADE,
    FOREIGN KEY (subTaskID) REFERENCES SubTask (id) ON DELETE CASCADE
);

-- ========================
-- Tabel: User_Project
-- Knytter en User til et Project
-- ========================
CREATE TABLE User_Project
(
    userID    INTEGER,
    projectID INTEGER,
    PRIMARY KEY (userID, projectID),
    FOREIGN KEY (userID) REFERENCES AppUser (id) ON DELETE CASCADE,
    FOREIGN KEY (projectID) REFERENCES Project (id) ON DELETE CASCADE
);

-- ========================
-- Tabel: User_Subproject
-- Knytter en User til et Subproject
-- ========================
CREATE TABLE User_Subproject
(
    userID       INTEGER,
    subProjectID INTEGER,
    PRIMARY KEY (userID, subProjectID),
    FOREIGN KEY (userID) REFERENCES AppUser (id) ON DELETE CASCADE,
    FOREIGN KEY (subProjectID) REFERENCES Subproject (id) ON DELETE CASCADE
);

-- ========================
-- Tabel: User_Task
-- Knytter en User til en Task
-- ========================
CREATE TABLE User_Task
(
    userID INTEGER,
    taskID INTEGER,
    PRIMARY KEY (userID, taskID),
    FOREIGN KEY (userID) REFERENCES AppUser (id) ON DELETE CASCADE,
    FOREIGN KEY (taskID) REFERENCES Task (id) ON DELETE CASCADE
);

-- ========================
-- Tabel: User_SubTask
-- Knytter en User til en SubTask
-- ========================
CREATE TABLE User_SubTask
(
    userID    INTEGER,
    subTaskID INTEGER,
    PRIMARY KEY (userID, subTaskID),
    FOREIGN KEY (userID) REFERENCES AppUser (id) ON DELETE CASCADE,
    FOREIGN KEY (subTaskID) REFERENCES SubTask (id) ON DELETE CASCADE
);

INSERT INTO AppUser (firstName, lastName, email, password, role)
VALUES ('TestAdmin', '1', 'test@1.com', '123', 'ADMIN'),
       ('TestManager', '2', 'test@2.com', '123', 'PROJECT_MANAGER'),
       ('TestUser', '3', 'test@3.com', '123', 'DEVELOPER');
INSERT INTO Project (name, description, deadline, estimatedTime, timeSpent, status)
VALUES ('Test Project 1', 'Et testprojekt 1', '2025-05-31', 100, 0, 'ACTIVE'),
       ('Test Project 2', 'Et testprojekt 2', '2025-05-31', 2500, 0, 'INACTIVE');

INSERT INTO Subproject (projectID, name, description, deadline, estimatedTime, timeSpent, status)
VALUES (1, 'Backend', 'Delprojekt: Backend', '2025-05-31', 80, 0, 'ACTIVE'),
       (2, 'Frontend', 'Delprojekt: Frontend', '2025-05-31', 80, 0, 'ACTIVE');

-- Indsæt tasks
INSERT INTO Task (subProjectID, name, description, deadline, estimatedTime, status)
VALUES (1, 'Backend_Task_1', 'Opgave 1', '2025-05-31', 10, 'ACTIVE'),
       (1, 'Backend_Task_2', 'Opgave 2', '2025-05-31', 10, 'ACTIVE'),
       (2, 'Frontend_Task_1', 'Opgave 1', '2025-05-31', 10, 'ACTIVE'),
       (2, 'Frontend_Task_2', 'Opgave 2', '2025-05-31', 10, 'ACTIVE');

-- Indsæt subtasks
INSERT INTO SubTask (taskID, name, description, deadline, estimatedTime, status)
VALUES (1, 'Task_1_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
       (1, 'Task_1_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
       (2, 'Task_2_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
       (2, 'Task_2_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
       (3, 'Task_3_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
       (3, 'Task_3_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE'),
       (4, 'Task_4_Sub_1', 'Underopgave 1', '2025-05-31', 2, 'ACTIVE'),
       (4, 'Task_4_Sub_2', 'Underopgave 2', '2025-05-31', 2, 'ACTIVE');

-- Indsæt time entries0æl
INSERT INTO TimeEntry (date, hoursSpent)
VALUES ('2025-05-01', 2),
       ('2025-05-01', 2),
       ('2025-05-01', 4),
       ('2025-05-01', 2),
       ('2025-05-01', 2),
       ('2025-05-01', 4),
       ('2025-05-01', 2),
       ('2025-05-01', 2);

INSERT INTO TimeEntry_Task (timeEntryID, taskID)
VALUES (2, 1),
       (5, 1),
       (8, 2);

INSERT INTO TimeEntry_SubTask (timeEntryID, subTaskID)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (6, 6),
       (7, 7),
       (8, 8);

INSERT INTO User_Project (userID, projectID)
VALUES (2, 1);

INSERT INTO User_Subproject (userID, subProjectID)
VALUES (3, 1),
       (3, 2);

INSERT INTO User_Task (userID, taskID)
VALUES (3, 1),
       (3, 2);

INSERT INTO User_SubTask (userID, subTaskID)
VALUES (3, 1),
       (3, 2),
       (3, 3),
       (3, 4);