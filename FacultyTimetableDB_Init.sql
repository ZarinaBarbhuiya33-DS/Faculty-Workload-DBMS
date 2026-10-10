-- ==========================================
-- DATABASE INITIALIZATION SCRIPT
-- Project: Faculty Workload & Timetable Intelligence System
-- DBMS: MySQL 8.0
-- ==========================================

CREATE DATABASE IF NOT EXISTS FacultyTimetableDB;
USE FacultyTimetableDB;

-- ==========================================
-- STEP 1: DROP EXISTING TABLES (Clean Setup)
-- ==========================================
DROP TABLE IF EXISTS Timetable;
DROP TABLE IF EXISTS Courses;
DROP TABLE IF EXISTS Faculty;

-- ==========================================
-- STEP 2: CREATE TABLES (DDL)
-- ==========================================
CREATE TABLE Faculty (
    Faculty_ID VARCHAR(15) PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Max_Workload INT
);

CREATE TABLE Courses (
    Course_Code VARCHAR(15) PRIMARY KEY,
    Course_Title VARCHAR(100) NOT NULL,
    Credits INT
);

CREATE TABLE Timetable (
    Schedule_ID VARCHAR(15) PRIMARY KEY,
    Faculty_ID VARCHAR(15),
    Course_Code VARCHAR(15),
    Room_No VARCHAR(10) NOT NULL,
    Time_Slot VARCHAR(20) NOT NULL,
    UNIQUE (Faculty_ID, Time_Slot),
    UNIQUE (Room_No, Time_Slot),
    FOREIGN KEY (Faculty_ID) REFERENCES Faculty(Faculty_ID) ON DELETE CASCADE,
    FOREIGN KEY (Course_Code) REFERENCES Courses(Course_Code) ON DELETE CASCADE
);

-- ==========================================
-- STEP 3: INSERT SAMPLE DATA (DML)
-- ==========================================
INSERT INTO Faculty (Faculty_ID, Name, Max_Workload) VALUES 
('FAC001', 'Dr. Naga Pawan', 12),
('FAC002', 'Prof. Alan Turing', 10),
('FAC003', 'Dr. Grace Hopper', 8);

INSERT INTO Courses (Course_Code, Course_Title, Credits) VALUES 
('DS201', 'Database Management Systems', 4),
('CS305', 'Object-Oriented Programming', 3),
('AI400', 'Artificial Intelligence', 4),
('SE300', 'Software Engineering', 3);

INSERT INTO Timetable (Schedule_ID, Faculty_ID, Course_Code, Room_No, Time_Slot) VALUES 
('SCH010', 'FAC001', 'SE300', 'Lab 1', 'Tue 2:00 PM'),
('SCH011', 'FAC001', 'AI400', 'Room 202', 'Wed 11:00 AM'),
('SCH012', 'FAC002', 'CS305', 'Room 102', 'Thu 10:00 AM');