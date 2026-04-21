-- ============================================================
-- Login Authentication System — Database Setup Script
-- ============================================================
-- Run this script in MySQL to create the database and table.
-- Usage: mysql -u root -p < setup.sql
-- ============================================================

-- Create the database
CREATE DATABASE IF NOT EXISTS auth_system;
USE auth_system;

-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    email           VARCHAR(100) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    failed_attempts INT          DEFAULT 0,
    account_locked  BOOLEAN      DEFAULT FALSE,
    created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Verify table creation
DESCRIBE users;

-- ============================================================
-- Expected Output:
-- +------------------+--------------+------+-----+-------------------+-------------------+
-- | Field            | Type         | Null | Key | Default           | Extra             |
-- +------------------+--------------+------+-----+-------------------+-------------------+
-- | id               | int          | NO   | PRI | NULL              | auto_increment    |
-- | username         | varchar(50)  | NO   | UNI | NULL              |                   |
-- | email            | varchar(100) | NO   | UNI | NULL              |                   |
-- | password_hash    | varchar(255) | NO   |     | NULL              |                   |
-- | failed_attempts  | int          | YES  |     | 0                 |                   |
-- | account_locked   | tinyint(1)   | YES  |     | 0                 |                   |
-- | created_at       | timestamp    | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
-- +------------------+--------------+------+-----+-------------------+-------------------+
-- ============================================================
