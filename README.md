# Faculty Workload & Timetable Intelligence System

A database-driven desktop application built to manage academic schedules, track faculty workloads, and prevent room/faculty double-booking through strict relational database constraints. 

## Technology Stack
* **Backend:** MySQL 8.0 (3NF Relational Schema)
* **Frontend:** Java Swing (Desktop GUI)
* **Connectivity:** JDBC (MySQL Connector/J)

## Features
* Schedule, substitute, or cancel classes.
* Enforce clash-free scheduling using database-native UNIQUE composite keys.
* Calculate active teaching credit workloads dynamically using SQL aggregates.
* Secure transaction execution via Java PreparedStatements.

## Setup Instructions
1. Run the `FacultyTimetableDB_Init.sql` script in MySQL to build the tables and insert sample data.
2. Ensure the MySQL JDBC Driver (`mysql-connector-j-8.x.x.jar`) is added to your Java project's build path/classpath.
3. Compile and run `Main.java` to launch the dashboard.
