DELETE FROM anomaly;
DELETE FROM import_error;
DELETE FROM import_job;
DELETE FROM invoice;
DELETE FROM supply_point;
DELETE FROM customer;
DELETE FROM users;

-- Sequence reset
SELECT setval('customer_seq', 100);
SELECT setval('supply_point_seq', 100);
SELECT setval('invoice_seq', 100);
SELECT setval('import_job_seq', 100);
SELECT setval('import_error_seq', 100);
SELECT setval('anomaly_seq', 100);
SELECT setval('users_seq', 100);

-- Users
INSERT INTO "users" (id, username, password, role, status, token, email, name, surname) VALUES
-- password: admin123
(1, 'admin', '$2a$10$I.CNmsIIJ6RMxG2Mqt1yc.3fm/zuR.bn2ne9EWz87SvzaW1dpBOO6', 'ADMIN', 'ACTIVE', null, 'admin@bguard.tech', 'Admin', 'Bguard'),
-- password: pass123
(2, 'analyst1', '$2a$10$ggMgx4vL/FP4tgFOdcWuz.t4LqxDnpAo/ne6azKCQfnMg6p4cd4R2', 'ANALYST', 'ACTIVE', null, 'analyst1@bguard.tech', 'Mario', 'Rossi'),
(3, 'viewer1', '$2a$10$ggMgx4vL/FP4tgFOdcWuz.t4LqxDnpAo/ne6azKCQfnMg6p4cd4R2', 'VIEWER', 'INACTIVE', null, 'viewer1@bguard.tech', 'Luca', 'Bianchi');

-- Customers
INSERT INTO customer (id, external_code, name, tax_code, email, created_at, updated_at) VALUES
(10, 'CUST-001', 'Acme Srl', '01234567890', 'acme@example.com', '2024-01-10 09:00:00', '2024-06-01 10:00:00'),
(11, 'CUST-002', 'Beta SpA', '09876543210', 'beta@example.com', '2024-02-15 09:00:00', '2024-06-01 10:00:00'),
(12, 'CUST-003', 'Gamma Srls', '11223344556', 'gamma@example.com', '2024-03-20 09:00:00', '2024-06-01 10:00:00');

-- Supply Points
INSERT INTO supply_point (id, code, type, region, city, status, created_at, updated_at, customer_id) VALUES
(20, 'SP-EL-001', 'ELECTRICITY', 'Lombardia', 'Milano', 'ACTIVE', '2024-01-15', '2024-06-01', 10),
(21, 'SP-GAS-001', 'GAS', 'Lombardia', 'Milano', 'ACTIVE', '2024-01-15', '2024-06-01', 10),
(22, 'SP-EL-002', 'ELECTRICITY', 'Lazio', 'Roma', 'ACTIVE', '2024-02-20', '2024-06-01', 11),
(23, 'SP-WAT-001', 'WATER', 'Lazio', 'Roma', 'SUSPENDED', '2024-02-20', '2024-06-01', 11),
(24, 'SP-GAS-002', 'GAS', 'Toscana', 'Firenze', 'INACTIVE', '2024-03-25', '2024-06-01', 12);

-- Invoices
INSERT INTO invoice (id, invoice_number, issue_date, due_date, payment_date, period_start, period_end, amount, paid_amount, status, supply_point_id, customer_id) VALUES
(30, 'INV-2024-001', '2024-01-31', '2024-02-28', '2024-02-20', '2024-01-01', '2024-01-31', 320.50, 320.50, 'PAID', 20, 10),
(31, 'INV-2024-002', '2024-02-29', '2024-03-31', null, '2024-02-01', '2024-02-29', 415.00, 0.00, 'UNPAID', 20, 10),
(32, 'INV-2024-003', '2024-01-31', '2024-02-28', '2024-02-25', '2024-01-01', '2024-01-31', 180.00, 100.00, 'PARTIALLY_PAID', 21, 10),
(33, 'INV-2024-004', '2024-02-29', '2024-03-31', '2024-03-15', '2024-02-01', '2024-02-29', 530.75, 530.75, 'PAID', 22, 11),
(34, 'INV-2024-005', '2024-03-31', '2024-04-30', null, '2024-03-01', '2024-03-31', 215.00, 0.00, 'ISSUED', 22, 11),
(35, 'INV-2024-006', '2024-03-31', '2024-04-30', null, '2024-03-01', '2024-03-31', -50.00, 0.00, 'CANCELLED', 24, 12);

-- Import Jobs
INSERT INTO import_job (id, filename, status, total_rows, processed_rows, discarded_rows, anomaly_rows, started_at, completed_at, error_message) VALUES
(40, 'import_jan_2024.csv', 'COMPLETED', 100, 98, 2, 3, '2024-01-05', '2024-01-05', null),
(41, 'import_feb_2024.csv', 'COMPLETED_WITH_ERRORS', 80, 75, 5, 8, '2024-02-05', '2024-02-05', 'Some rows could not be processed'),
(42, 'import_mar_2024.csv', 'FAILED', 60, 10, 0, 0, '2024-03-05', null, 'File format invalid at row 11');

-- Import Errors
INSERT INTO import_error (id, row_number, field_name, raw_value, error_code, message, created_at, import_job_id) VALUES
(50, 5, 'amount', 'N/A', 'INVALID_NUMBER', 'Cannot parse amount field', '2024-01-05', 40),
(51, 12, 'issue_date', '31/02/2024', 'INVALID_DATE', 'Date 31/02/2024 does not exist', '2024-01-05', 40),
(52, 3, 'customer_id', '9999', 'NOT_FOUND', 'Customer 9999 not found', '2024-02-05', 41),
(53, 7, 'tax_code', '', 'REQUIRED_FIELD', 'Tax code is required', '2024-02-05', 41),
(54, 11, 'invoice_number', 'INV-2024-002', 'DUPLICATE', 'Invoice number already exists', '2024-03-05', 42);

-- Anomalies
INSERT INTO anomaly (id, type, severity, status, description, technical_details, resolved_by, resolved_at, created_at, invoice_id, supply_point_id, import_job_id) VALUES
(60, 'NEGATIVE_AMOUNT', 'HIGH', 'RESOLVED', 'Invoice INV-2024-006 has negative amount', 'amount=-50.00', 'admin', '2024-04-10 10:00:00', '2024-04-01 09:00:00', 35, null, 40),
(61, 'UNPAID_OVER_THRESHOLD', 'CRITICAL', 'OPEN', 'Invoice INV-2024-002 unpaid over 30 days', 'due_date=2024-03-31, days_overdue=45', null, null, '2024-05-15 09:00:00', 31, 20, null),
(62, 'DUPLICATE_INVOICE', 'MEDIUM', 'IN_REVIEW', 'Possible duplicate of INV-2024-001', 'Same period and supply point', null, null, '2024-03-05 09:00:00', 31, 20, 42),
(63, 'MISSING_SUPPLY_POINT', 'LOW', 'IGNORED', 'Supply point SP-WAT-001 is suspended', 'status=SUSPENDED', 'analyst1', '2024-05-01 11:00:00', '2024-04-20 09:00:00', null, 23, null);