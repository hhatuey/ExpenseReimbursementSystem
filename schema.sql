-- PostgreSQL Database Schema for Expense Reimbursement System
-- Created based on Java DAO analysis

-- Drop existing tables if they exist (for clean recreation)
DROP TABLE IF EXISTS reimb_view CASCADE;
DROP TABLE IF EXISTS reimbursement CASCADE;
DROP TABLE IF EXISTS reimbursement_status CASCADE;
DROP TABLE IF EXISTS reimbursement_type CASCADE;
DROP TABLE IF EXISTS ers_user CASCADE;
DROP FUNCTION IF EXISTS create_view() CASCADE;

-- Create User Roles Table (implied from User model)
CREATE TABLE user_roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
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

-- Create Reimbursement Types Table
CREATE TABLE reimbursement_type (
    type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create Reimbursement Status Table
CREATE TABLE reimbursement_status (
    status_id SERIAL PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL UNIQUE
);

-- Create Reimbursements Table
CREATE TABLE reimbursement (
    reimb_id SERIAL PRIMARY KEY,
    reimb_amount DECIMAL(10,2) NOT NULL,
    reimb_submitted TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reimb_resolved TIMESTAMP,
    reimb_description TEXT,
    reimb_receipt INTEGER,
    reimb_author INTEGER NOT NULL REFERENCES ers_user(ers_user_id),
    reimb_resolver INTEGER REFERENCES ers_user(ers_user_id),
    reimb_status_id INTEGER NOT NULL REFERENCES reimbursement_status(status_id),
    reimb_type_id INTEGER NOT NULL REFERENCES reimbursement_type(type_id),
    reimb_sign VARCHAR(255)
);

-- Create View for Expenses (as used in ExpensesDao)
CREATE VIEW reimb_view AS
SELECT 
    r.reimb_id AS id,
    r.reimb_amount AS amount,
    r.reimb_submitted AS submitted,
    r.reimb_resolved AS resolved,
    r.reimb_description AS description,
    r.reimb_receipt AS receipt,
    ur1.role_name AS role1,
    CONCAT(u1.user_first_name, ' ', u1.user_last_name) AS author,
    COALESCE(ur2.role_name, '') AS role2,
    CONCAT(COALESCE(u2.user_first_name, ''), ' ', COALESCE(u2.user_last_name, '')) AS resolver,
    rs.status_name AS status,
    rt.type_name AS type
FROM reimbursement r
JOIN ers_user u1 ON r.reimb_author = u1.ers_user_id
JOIN user_roles ur1 ON u1.user_role_id = ur1.role_id
LEFT JOIN ers_user u2 ON r.reimb_resolver = u2.ers_user_id
LEFT JOIN user_roles ur2 ON u2.user_role_id = ur2.role_id
JOIN reimbursement_status rs ON r.reimb_status_id = rs.status_id
JOIN reimbursement_type rt ON r.reimb_type_id = rt.type_id;

-- Create stored procedure to refresh the view (as referenced in ExpensesDao)
CREATE OR REPLACE FUNCTION create_view()
RETURNS VOID AS $$
BEGIN
    -- The view is already created, but this function exists for compatibility
    -- with the Java code that calls it
    NULL;
END;
$$ LANGUAGE plpgsql;

-- Insert default data
-- User Roles
INSERT INTO user_roles (role_name) VALUES ('EMPLOYEE'), ('MANAGER');

-- Reimbursement Types
INSERT INTO reimbursement_type (type_name) VALUES 
('LODGING'), 
('TRAVEL'), 
('FOOD'), 
('OTHER');

-- Reimbursement Status
INSERT INTO reimbursement_status (status_name) VALUES 
('PENDING'), 
('APPROVED'), 
('DENIED');

-- Create indexes for better performance
CREATE INDEX idx_reimbursement_author ON reimbursement(reimb_author);
CREATE INDEX idx_reimbursement_status ON reimbursement(reimb_status_id);
CREATE INDEX idx_reimbursement_type ON reimbursement(reimb_type_id);
CREATE INDEX idx_reimbursement_submitted ON reimbursement(reimb_submitted);
CREATE INDEX idx_user_username ON ers_user(ers_username);
CREATE INDEX idx_user_email ON ers_user(user_email);

-- Add constraints for data integrity
ALTER TABLE reimbursement ADD CONSTRAINT chk_reimb_amount_positive CHECK (reimb_amount > 0);
ALTER TABLE ers_user ADD CONSTRAINT chk_username_not_empty CHECK (length(ers_username) > 0);
ALTER TABLE ers_user ADD CONSTRAINT chk_email_format CHECK (user_email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');

-- Grant permissions (adjust as needed for your setup)
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO your_app_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO your_app_user;