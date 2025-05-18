-- Dropper tables hvis de findes for at rydde al data

DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS SubpPoject;
DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS SubTask;
DROP TABLE IF EXISTS TimeEntry;

DROP TABLE IF EXISTS Users_Project;
DROP TABLE IF EXISTS Users_SubProject;
DROP TABLE IF EXISTS User_Task;
DROP TABLE IF EXISTS User_SubTask;




-- ========================
-- Tabel: Users
-- Gemmer information om en bruger
-- ========================
CREATE TABLE Users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    firstName       VARCHAR(255),
    lastName        VARCHAR(255),
    email           VARCHAR(255) UNIQUE,
    passwordHash    VARCHAR(255),
    role            VARCHAR(255) CHECK (role IN ('ADMIN', 'PROJECT_MANAGER', 'DEVELOPER'))
);

-- ========================
-- Tabel: Project
-- Gemmer information om et projekt
-- ========================
CREATE TABLE Project (
     id              INT AUTO_INCREMENT PRIMARY KEY,
     name            VARCHAR(255) UNIQUE,
     description     VARCHAR(255),
     deadline        DATE,
     estimatedTime   INTEGER,
     timeSpent       INTEGER,
     status          VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE'))
);

-- ========================
-- Tabel: Subproject
-- ========================
CREATE TABLE SubProject (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    projectID       INTEGER,
    name            VARCHAR(255),
    description     VARCHAR(255),
    deadline        DATE,
    estimatedTime   INTEGER,
    timeSpent       INTEGER,
    status          VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE')),

    UNIQUE (projectID, name),
    FOREIGN KEY (projectID) REFERENCES Project(id) ON DELETE CASCADE
);

-- ========================
-- Tabel: Task
-- ========================
CREATE TABLE Task (
      id              INT AUTO_INCREMENT PRIMARY KEY,
      subProjectID    INTEGER,
      name            VARCHAR(255),
      description     VARCHAR(255),
      deadline        DATE,
      estimatedTime   INTEGER,
      timeSpent       INTEGER,
      status          VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE')),

      UNIQUE (subProjectID, name),
      FOREIGN KEY (subProjectID) REFERENCES SubProject(id) ON DELETE CASCADE
);

-- ========================
-- Tabel: SubTask
-- ========================
CREATE TABLE SubTask (
     id              INT AUTO_INCREMENT PRIMARY KEY,
     taskID          INTEGER,
     name            VARCHAR(255),
     description     VARCHAR(255),
     deadline        DATE,
     estimatedTime   INTEGER,
     timeSpent       INTEGER,
     status          VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE')),

     UNIQUE (taskID, name),
     FOREIGN KEY (taskID) REFERENCES Task(id) ON DELETE CASCADE
);

-- ========================
-- Tabel: TimeEntry
-- ========================
CREATE TABLE TimeEntry (
       id          INT AUTO_INCREMENT PRIMARY KEY,
       userID      INTEGER,
       taskID      INTEGER NULL,
       subtaskID   INTEGER NULL,
       date        DATE,
       hoursSpent  INTEGER,

       FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE
);

-- ========================
-- Knytninger mellem bruger og projekter / opgaver
-- ========================
CREATE TABLE Users_Project (
     userID      INTEGER,
     projectID   INTEGER,

     PRIMARY KEY (userID, projectID),
     FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE,
     FOREIGN KEY (projectID) REFERENCES Project(id) ON DELETE CASCADE
);

CREATE TABLE Users_SubProject (
    userID          INTEGER,
    subProjectID    INTEGER,

    PRIMARY KEY (userID, subProjectID),
    FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (subProjectID) REFERENCES SubProject(id) ON DELETE CASCADE
);

CREATE TABLE Users_Task (
    userID  INTEGER,
    taskID  INTEGER,

    PRIMARY KEY (userID, taskID),
    FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (taskID) REFERENCES Task(id) ON DELETE CASCADE
);

CREATE TABLE Users_SubTask (
     userID      INTEGER,
     subTaskID   INTEGER,

     PRIMARY KEY (userID, subTaskID),
     FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE,
     FOREIGN KEY (subTaskID) REFERENCES SubTask(id) ON DELETE CASCADE
);

-- ========================
-- Testdata
-- ========================

INSERT INTO Users (firstName, lastName, email, passwordHash, role)
VALUES ('TestAdmin', '1', 'test@1.com', '$2a$12$WshmMsov744g1iD93L.3tuJkScgrJ4q4VgIYuiZZoKwuNK42/9Tiu', 'ADMIN'),
       ('TestManager', '2', 'test@2.com', '$2a$12$d.2NywpcgVwm1PQ2XHb.t.t8WGCWH/oLuFWDWn8esNhQbUXXvm.Im', 'PROJECT_MANAGER'),
       ('TestUser1', '3', 'test@3.com', '$2a$12$oQd7HAhZZZeWODrTeF3ube1y3VnCP2jxy2py7EMMV4eGZmcMBlodm', 'DEVELOPER'),
       ('TestUser2', '4', 'test@4.com', '$2a$12$oQd7HAhZZZeWODrTeF3ube1y3VnCP2jxy2py7EMMV4eGZmcMBlodm', 'DEVELOPER');

INSERT INTO Project (name, description, deadline, estimatedTime, timeSpent, status)
VALUES ('Test Project 1', 'Et testprojekt 1', '2025-05-31', 100, 0, 'ACTIVE'),
       ('Test Project 2', 'Et testprojekt 2', '2025-05-31', 2500, 0, 'INACTIVE');

INSERT INTO Subproject (projectID, name, description, deadline, estimatedTime, timeSpent, status)
VALUES (1, 'Backend', 'Delprojekt: Backend', '2025-05-31', 80, 0, 'ACTIVE'),
       (2, 'Frontend', 'Delprojekt: Frontend', '2025-05-31', 80, 0, 'ACTIVE');

INSERT INTO Task (subProjectID, name, description, deadline, estimatedTime, timeSpent, status)
VALUES
    (1, 'Backend_Task_1', 'Opgave 1', '2025-05-31', 10, 0, 'ACTIVE'),
    (1, 'Backend_Task_2', 'Opgave 2', '2025-05-31', 10, 0, 'ACTIVE'),
    (2, 'Frontend_Task_1', 'Opgave 1', '2025-05-31', 10, 0, 'ACTIVE'),
    (2, 'Frontend_Task_2', 'Opgave 2', '2025-05-31', 10, 0, 'ACTIVE');

INSERT INTO SubTask (taskID, name, description, deadline, estimatedTime, timeSpent, status)
VALUES
    (1, 'Task_1_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
    (1, 'Task_1_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
    (2, 'Task_2_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
    (2, 'Task_2_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
    (3, 'Task_3_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
    (3, 'Task_3_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE'),
    (4, 'Task_4_Sub_1', 'Underopgave 1', '2025-05-31', 2, 0, 'ACTIVE'),
    (4, 'Task_4_Sub_2', 'Underopgave 2', '2025-05-31', 2, 0, 'ACTIVE');

INSERT INTO TimeEntry (userID, taskID, subtaskID, date, hoursSpent)
VALUES
    (3, 1, 1, '2025-05-01', 2),
    (3, 1, 2, '2025-05-01', 2),
    (3, 2, 3, '2025-05-01', 2),
    (3, 2, 4, '2025-05-01', 2),
    (3, 3, 5, '2025-05-01', 2),
    (3, 3, 6, '2025-05-01', 2),
    (3, 4, 7, '2025-05-01', 2),
    (3, 4, 8, '2025-05-01', 2);

INSERT INTO Users_Project (userID, projectID)
VALUES (2, 1),
       (3, 1);

INSERT INTO Users_Subproject (userID, subProjectID)
VALUES (2, 1),
       (3, 1),
       (2, 2),
       (3, 2);

INSERT INTO Users_Task (userID, taskID)
VALUES (3, 1),
       (3, 2);

INSERT INTO Users_SubTask (userID, subTaskID)
VALUES (3, 1),
       (3, 2),
       (3, 3),
       (3, 4);