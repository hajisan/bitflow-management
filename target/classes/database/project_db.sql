-- Opretter database, hvis den ikke allerede findes
CREATE DATABASE IF NOT EXISTS project_db;
USE project_db;

-- Dropper tables hvis de findes for at rydde al data
-- Drop timeentry først --
DROP TABLE IF EXISTS timeentry;
-- Derefter drop Users_-tabellerne -- 
DROP TABLE IF EXISTS users_project;
DROP TABLE IF EXISTS users_subproject;
DROP TABLE IF EXISTS users_task;
DROP TABLE IF EXISTS users_subtask;
-- Derefter drop Users --
DROP TABLE IF EXISTS users;
-- Til sidst droppes projektkomponenttabellerne fra lavest til højest -- 
DROP TABLE IF EXISTS subtask;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS subproject;
DROP TABLE IF EXISTS project;




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
) AUTO_INCREMENT = 1;

-- ========================
-- Tabel: Project
-- Gemmer information om et Project
-- ========================
CREATE TABLE Project (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) UNIQUE,
    description     VARCHAR(255),
    deadline        DATE,
    estimatedTime   INTEGER,
    timeSpent       INTEGER,
    status          VARCHAR(255) CHECK (status IN ('ACTIVE', 'INACTIVE', 'DONE'))
) AUTO_INCREMENT = 1000;

-- ========================
-- Tabel: subProject
-- Gemmer information om et subProject
-- ========================
CREATE TABLE Subproject (
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
) AUTO_INCREMENT = 10000;

-- ========================
-- Tabel: Task
-- Gemmer information om en Task
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
    FOREIGN KEY (subProjectID) REFERENCES Subproject(id) ON DELETE CASCADE
) AUTO_INCREMENT = 100000;

-- ========================
-- Tabel: SubTask
-- Gemmer information om en subTask
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
) AUTO_INCREMENT = 1000000;

-- ========================
-- Tabel: timeEntry
-- Gemmer information om en timeEntry
-- ========================
CREATE TABLE TimeEntry (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    userID      INTEGER,
    taskID      INTEGER,
    subtaskID   INTEGER,
    date        DATE,
    hoursSpent  INTEGER,

    FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE
) AUTO_INCREMENT = 10000000;

-- ========================
-- Tabel: Users_Project
-- Knytter en User til et Project
-- ========================
CREATE TABLE Users_Project (
    userID      INTEGER,
    projectID   INTEGER,

    PRIMARY KEY (userID, projectID),
    FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (projectID) REFERENCES Project(id) ON DELETE CASCADE
);

-- ========================
-- Tabel: Users_Subproject
-- Knytter en User til et Subproject
-- ========================
CREATE TABLE Users_SubProject (
    userID          INTEGER,
    subProjectID    INTEGER,

    PRIMARY KEY (userID, subProjectID),
    FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (subProjectID) REFERENCES SubProject(id) ON DELETE CASCADE
);

-- ========================
-- Tabel: Users_Task
-- Knytter en User til en Task
-- ========================
CREATE TABLE Users_Task (
    userID  INTEGER,
    taskID  INTEGER,

    PRIMARY KEY (userID, taskID),
    FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (taskID) REFERENCES Task(id) ON DELETE CASCADE
);

-- ========================
-- Tabel: Users_SubTask
-- Knytter en User til en SubTask
-- ========================
CREATE TABLE Users_SubTask (
    userID      INTEGER,
    subTaskID   INTEGER,

    PRIMARY KEY (userID, subTaskID),
    FOREIGN KEY (userID) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (subTaskID) REFERENCES SubTask(id) ON DELETE CASCADE
);

