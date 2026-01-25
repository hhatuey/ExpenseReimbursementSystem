-- PostgreSQL Database Schema for Expense Reimbursement System (ERS)
-- Generated based on Java DAO classes and models

-- Create database
-- CREATE DATABASE ers_db;
-- \c ers_db;

-- Drop existing tables if they exist (for fresh deployment)
DROP FUNCTION IF EXISTS create_view() CASCADE;
DROP VIEW IF EXISTS reimb_view CASCADE;
DROP TABLE IF EXISTS reimbursement CASCADE;
DROP TABLE IF EXISTS reimbursement_status CASCADE;
DROP TABLE IF EXISTS reimbursement_type CASCADE;
DROP TABLE IF EXISTS ers_user CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;

-- Create User Roles Table
-- Note: This table is referenced but not explicitly created in DAOs
-- Creating based on enum RoleType in User model
CREATE TABLE user_roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL UNIQUE
);

-- Create Users Table
CREATE TABLE ers_user (
    ers_user_id SERIAL PRIMARY KEY,
    ers_username VARCHAR(50) NOT NULL UNIQUE,
    ers_password VARCHAR(255) NOT NULL,
    user_first_name VARCHAR(100) NOT NULL,
    user_last_name VARCHAR(100) NOT NULL,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    user_role_id INTEGER NOT NULL REFERENCES user_roles(role_id)
);

-- Create Reimbursement Status Table
CREATE TABLE reimbursement_status (
    reimb_status_id SERIAL PRIMARY KEY,
    reimb_status VARCHAR(20) NOT NULL UNIQUE
);

-- Create Reimbursement Type Table
CREATE TABLE reimbursement_type (
    reimb_type_id SERIAL PRIMARY KEY,
    reimb_type VARCHAR(50) NOT NULL UNIQUE
);

-- Create Reimbursement Table
CREATE TABLE reimbursement (
    reimb_id SERIAL PRIMARY KEY,
    reimb_amount DECIMAL(10, 2) NOT NULL,
    reimb_submitted TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reimb_resolved TIMESTAMP,
    reimb_description TEXT,
    reimb_receipt INTEGER DEFAULT 0,
    reimb_author INTEGER NOT NULL REFERENCES ers_user(ers_user_id),
    reimb_resolver INTEGER REFERENCES ers_user(ers_user_id),
    reimb_status_id INTEGER NOT NULL DEFAULT 1 REFERENCES reimbursement_status(reimb_status_id),
    reimb_type_id INTEGER NOT NULL REFERENCES reimbursement_type(reimb_type_id),
    reimb_sign VARCHAR(255)
);

-- Create View for Expenses (used in ExpensesDao)
-- This view joins reimbursement with user and lookup tables
CREATE VIEW reimb_view AS
SELECT 
    r.reimb_id,
    r.reimb_amount,
    r.reimb_submitted,
    r.reimb_resolved,
    r.reimb_description,
    r.reimb_receipt,
    ur1.role_name AS role1,
    u1.user_first_name || ' ' || u1.user_last_name AS author,
    ur2.role_name AS role2,
    u2.user_first_name || ' ' || u2.user_last_name AS resolver,
    rs.reimb_status AS status,
    rt.reimb_type AS type
FROM reimbursement r
JOIN ers_user u1 ON r.reimb_author = u1.ers_user_id
JOIN user_roles ur1 ON u1.user_role_id = ur1.role_id
LEFT JOIN ers_user u2 ON r.reimb_resolver = u2.ers_user_id
LEFT JOIN user_roles ur2 ON u2.user_role_id = ur2.role_id
JOIN reimbursement_status rs ON r.reimb_status_id = rs.reimb_status_id
JOIN reimbursement_type rt ON r.reimb_type_id = rt.reimb_type_id;

-- Create Stored Procedure to refresh the view (used in ExpensesDao)
CREATE OR REPLACE FUNCTION create_view() 
RETURNS VOID AS $$
BEGIN
    -- Refresh materialized view if needed, or ensure view is up to date
    -- For regular view, this is a no-op but maintains compatibility
    PERFORM 1;
END;
$$ LANGUAGE plpgsql;

-- Insert initial data into lookup tables

-- Insert user roles
INSERT INTO user_roles (role_name) VALUES 
    ('EMPLOYEE'),
    ('MANAGER');

-- Insert reimbursement statuses
INSERT INTO reimbursement_status (reimb_status) VALUES 
    ('PENDING'),
    ('APPROVED'),
    ('DENIED');

-- Insert reimbursement types
INSERT INTO reimbursement_type (reimb_type) VALUES 
    ('LODGING'),
    ('TRAVEL'),
    ('FOOD'),
    ('OTHER');

-- Insert sample users
-- Passwords should be hashed in real application
INSERT INTO ers_user (ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) VALUES
    ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'Admin', 'User', 'admin@ers.com', 2),
    ('employee1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'John', 'Doe', 'john.doe@ers.com', 1),
    ('employee2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'Jane', 'Smith', 'jane.smith@ers.com', 1),
    ('manager1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'Mike', 'Wilson', 'mike.wilson@ers.com', 2);

-- Insert sample reimbursement data
INSERT INTO reimbursement (reimb_amount, reimb_submitted, reimb_description, reimb_author, reimb_status_id, reimb_type_id) VALUES
    (150.00, CURRENT_TIMESTAMP - INTERVAL '7 days', 'Hotel stay for business trip', 2, 1, 1),
    (75.50, CURRENT_TIMESTAMP - INTERVAL '5 days', 'Airfare for conference', 2, 2, 2),
    (45.25, CURRENT_TIMESTAMP - INTERVAL '3 days', 'Team lunch meeting', 3, 1, 3),
    (200.00, CURRENT_TIMESTAMP - INTERVAL '2 days', 'Training materials', 3, 3, 4),
    (120.00, CURRENT_TIMESTAMP - INTERVAL '1 day', 'Taxi expenses', 2, 1, 2);

-- Update some reimbursements with resolver information
UPDATE reimbursement 
SET reimb_resolved = CURRENT_TIMESTAMP - INTERVAL '4 days', reimb_resolver = 4
WHERE reimb_id = 2;

UPDATE reimbursement 
SET reimb_resolved = CURRENT_TIMESTAMP - INTERVAL '1 day', reimb_resolver = 4
WHERE reimb_id = 4;

-- Create indexes for better performance
CREATE INDEX idx_reimbursement_author ON reimbursement(reimb_author);
CREATE INDEX idx_reimbursement_status ON reimbursement(reimb_status_id);
CREATE INDEX idx_reimbursement_type ON reimbursement(reimb_type_id);
CREATE INDEX idx_reimbursement_submitted ON reimbursement(reimb_submitted);
CREATE INDEX idx_ers_user_username ON ers_user(ers_username);
CREATE INDEX idx_ers_user_email ON ers_user(user_email);

-- Grant permissions (adjust as needed for your environment)
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ers_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ers_user;

-- Display table information for verification
SELECT 
    table_name,
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns 
WHERE table_schema = 'public' 
    AND table_name IN ('ers_user', 'reimbursement', 'reimbursement_status', 'reimbursement_type', 'user_roles')
ORDER BY table_name, ordinal_position;

-- Display view structure information
SELECT 
    table_name,
    column_name,
    data_type
FROM information_schema.columns 
WHERE table_schema = 'public' 
    AND table_name = 'reimb_view'
ORDER BY ordinal_position;

-- Display sample data
SELECT 'Users:' as info;
SELECT ers_user_id, ers_username, user_first_name, user_last_name, user_email, ur.role_name 
FROM ers_user u
JOIN user_roles ur ON u.user_role_id = ur.role_id;

SELECT 'Reimbursements:' as info;
SELECT r.reimb_id, r.reimb_amount, r.reimb_submitted, rs.reimb_status, rt.reimb_type, 
       u1.user_first_name || ' ' || u1.user_last_name as author
FROM reimbursement r
JOIN reimbursement_status rs ON r.reimb_status_id = rs.reimb_status_id
JOIN reimbursement_type rt ON r.reimb_type_id = rt.reimb_type_id
JOIN ers_user u1 ON r.reimb_author = u1.ers_user_id;