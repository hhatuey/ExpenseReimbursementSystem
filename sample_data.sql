-- Sample Data Insertion Script for Expense Reimbursement System
-- This script populates the database with realistic test data

-- Insert sample users with different roles
-- Employees
INSERT INTO ers_user (ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) VALUES
('jsmith', 'password123', 'John', 'Smith', 'john.smith@company.com', 1),
('sjohnson', 'password123', 'Sarah', 'Johnson', 'sarah.johnson@company.com', 1),
('mwilliams', 'password123', 'Michael', 'Williams', 'michael.williams@company.com', 1),
('dbrown', 'password123', 'David', 'Brown', 'david.brown@company.com', 1),
('ljones', 'password123', 'Lisa', 'Jones', 'lisa.jones@company.com', 1),
('rgarcia', 'password123', 'Robert', 'Garcia', 'robert.garcia@company.com', 1),
('amiller', 'password123', 'Anna', 'Miller', 'anna.miller@company.com', 1),
('cwilson', 'password123', 'Charles', 'Wilson', 'charles.wilson@company.com', 1);

-- Managers
INSERT INTO ers_user (ers_username, ers_password, user_first_name, user_last_name, user_email, user_role_id) VALUES
('janderson', 'manager123', 'Jennifer', 'Anderson', 'jennifer.anderson@company.com', 2),
('ptaylor', 'manager123', 'Paul', 'Taylor', 'paul.taylor@company.com', 2);

-- Insert sample reimbursements with various statuses and types
-- Pending reimbursements
INSERT INTO reimbursement (reimb_amount, reimb_submitted, reimb_description, reimb_receipt, reimb_author, reimb_status_id, reimb_type_id, reimb_sign) VALUES
(250.00, CURRENT_TIMESTAMP - INTERVAL '7 days', 'Hotel accommodation for business trip to Chicago', 10234, 1, 1, 1, 'MQ=='),
(89.50, CURRENT_TIMESTAMP - INTERVAL '5 days', 'Client dinner at Italian restaurant', 10235, 2, 1, 3, 'MQ=='),
(145.75, CURRENT_TIMESTAMP - INTERVAL '3 days', 'Taxi fare to airport', 10236, 1, 1, 2, 'MQ=='),
(320.00, CURRENT_TIMESTAMP - INTERVAL '2 days', 'Conference registration fee', 10237, 3, 1, 4, 'MQ=='),
(67.25, CURRENT_TIMESTAMP - INTERVAL '1 day', 'Team lunch for project celebration', 10238, 4, 1, 3, 'MQ=='),
(450.00, CURRENT_TIMESTAMP - INTERVAL '12 hours', 'Flight to Denver for client meeting', 10239, 5, 1, 2, 'MQ==');

-- Approved reimbursements
INSERT INTO reimbursement (reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id, reimb_sign) VALUES
(180.00, CURRENT_TIMESTAMP - INTERVAL '14 days', CURRENT_TIMESTAMP - INTERVAL '10 days', 'Hotel stay in New York', 10240, 1, 9, 2, 1, 'MQ=='),
(120.00, CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP - INTERVAL '8 days', 'Train tickets to Boston', 10241, 2, 9, 2, 2, 'MQ=='),
(95.00, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '6 days', 'Business lunch with partner', 10242, 3, 10, 2, 3, 'MQ=='),
(500.00, CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '4 days', 'Trade show booth rental', 10243, 4, 9, 2, 4, 'MQ=='),
(75.50, CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Parking fees for downtown meeting', 10244, 5, 10, 2, 2, 'MQ=='),
(210.00, CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Hotel accommodation for training', 10245, 6, 9, 2, 1, 'MQ==');

-- Denied reimbursements
INSERT INTO reimbursement (reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id, reimb_sign) VALUES
(300.00, CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '15 days', 'Personal vacation expenses', 10246, 1, 9, 3, 4, 'MQ=='),
(150.00, CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP - INTERVAL '13 days', 'Family dinner claimed as business expense', 10247, 2, 10, 3, 3, 'MQ=='),
(80.00, CURRENT_TIMESTAMP - INTERVAL '16 days', CURRENT_TIMESTAMP - INTERVAL '11 days', 'Golf outing with friends', 10248, 3, 9, 3, 4, 'MQ==');

-- Additional diverse sample data
-- More approved expenses
INSERT INTO reimbursement (reimb_amount, reimb_submitted, reimb_resolved, reimb_description, reimb_receipt, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id, reimb_sign) VALUES
(275.00, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '25 days', 'Airfare to San Francisco', 10249, 7, 10, 2, 2, 'MQ=='),
(125.00, CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP - INTERVAL '23 days', 'Conference materials and supplies', 10250, 8, 9, 2, 4, 'MQ=='),
(55.00, CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP - INTERVAL '21 days', 'Internet access during travel', 10251, 1, 10, 2, 2, 'MQ=='),
(195.00, CURRENT_TIMESTAMP - INTERVAL '24 days', CURRENT_TIMESTAMP - INTERVAL '19 days', 'Car rental for business trip', 10252, 2, 9, 2, 2, 'MQ==');

-- More pending expenses
INSERT INTO reimbursement (reimb_amount, reimb_submitted, reimb_description, reimb_receipt, reimb_author, reimb_status_id, reimb_type_id, reimb_sign) VALUES
(420.00, CURRENT_TIMESTAMP - INTERVAL '1 hour', 'Software license renewal', 10253, 4, 1, 4, 'MQ=='),
(175.00, CURRENT_TIMESTAMP - INTERVAL '3 hours', 'Client entertainment expenses', 10254, 5, 1, 3, 'MQ=='),
(290.00, CURRENT_TIMESTAMP - INTERVAL '6 hours', 'Hotel for upcoming conference', 10255, 6, 1, 1, 'MQ=='),
(65.00, CURRENT_TIMESTAMP - INTERVAL '9 hours', 'Office supplies for project', 10256, 7, 1, 4, 'MQ=='),
(110.00, CURRENT_TIMESTAMP - INTERVAL '15 hours', 'Mileage reimbursement for client visits', 10257, 8, 1, 2, 'MQ==');

-- High-value expenses
INSERT INTO reimbursement (reimb_amount, reimb_submitted, reimb_description, reimb_receipt, reimb_author, reimb_status_id, reimb_type_id, reimb_sign) VALUES
(1500.00, CURRENT_TIMESTAMP - INTERVAL '2 days', 'International flight to London', 10258, 1, 1, 2, 'MQ=='),
(875.00, CURRENT_TIMESTAMP - INTERVAL '4 days', 'Week-long hotel stay for project', 10259, 2, 1, 1, 'MQ=='),
(2200.00, CURRENT_TIMESTAMP - INTERVAL '6 days', 'Executive training program fee', 10260, 3, 1, 4, 'MQ==');

-- Small, frequent expenses
INSERT INTO reimbursement (reimb_amount, reimb_submitted, reimb_description, reimb_receipt, reimb_author, reimb_status_id, reimb_type_id, reimb_sign) VALUES
(15.50, CURRENT_TIMESTAMP - INTERVAL '1 hour', 'Coffee with client', 10261, 4, 1, 3, 'MQ=='),
(22.75, CURRENT_TIMESTAMP - INTERVAL '2 hours', 'Parking meter fees', 10262, 5, 1, 2, 'MQ=='),
(35.00, CURRENT_TIMESTAMP - INTERVAL '3 hours', 'Office lunch', 10263, 6, 1, 3, 'MQ=='),
(18.25, CURRENT_TIMESTAMP - INTERVAL '4 hours', 'Printing costs', 10264, 7, 1, 4, 'MQ=='),
(42.00, CURRENT_TIMESTAMP - INTERVAL '5 hours', 'Ride share to meeting', 10265, 8, 1, 2, 'MQ==');

-- Create a sequence for auto-generating receipt numbers (optional)
CREATE SEQUENCE IF NOT EXISTS receipt_number_seq START WITH 10266;

-- Set the current value of the receipt sequence
SELECT setval('receipt_number_seq', (SELECT MAX(reimb_receipt) FROM reimbursement WHERE reimb_receipt IS NOT NULL));

-- Query to verify data insertion
SELECT 'Users:' as table_name, COUNT(*) as record_count FROM ers_user
UNION ALL
SELECT 'Reimbursements:', COUNT(*) FROM reimbursement
UNION ALL
SELECT 'Pending:', COUNT(*) FROM reimbursement WHERE reimb_status_id = 1
UNION ALL
SELECT 'Approved:', COUNT(*) FROM reimbursement WHERE reimb_status_id = 2
UNION ALL
SELECT 'Denied:', COUNT(*) FROM reimbursement WHERE reimb_status_id = 3;

-- Sample queries to test the data
-- View all reimbursements with user details
SELECT 
    r.reimb_id,
    r.reimb_amount,
    u1.ers_username as author,
    u2.ers_username as resolver,
    rs.status_name,
    rt.type_name,
    r.reimb_submitted,
    r.reimb_resolved
FROM reimbursement r
JOIN ers_user u1 ON r.reimb_author = u1.ers_user_id
LEFT JOIN ers_user u2 ON r.reimb_resolver = u2.ers_user_id
JOIN reimbursement_status rs ON r.reimb_status_id = rs.status_id
JOIN reimbursement_type rt ON r.reimb_type_id = rt.type_id
ORDER BY r.reimb_submitted DESC;

-- View pending reimbursements by manager Jennifer Anderson (ID=9)
SELECT 
    r.reimb_id,
    r.reimb_amount,
    r.reimb_description,
    u.ers_username,
    r.reimb_submitted
FROM reimbursement r
JOIN ers_user u ON r.reimb_author = u.ers_user_id
WHERE r.reimb_status_id = 1
ORDER BY r.reimb_submitted DESC;