-- Inserimento Customers
INSERT INTO customer (id, external_code, name, tax_code, email, created_at, updated_at) VALUES
(1001, 'EXT-TEST-001', 'Mario Rossi', 'RSSMRA80A01H501Z', 'mario.test@example.com', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(1002, 'EXT-TEST-002', 'Luigi Bianchi', 'BNCLGU90B02H501Y', 'luigi.test@example.com', '2024-02-01 10:00:00', '2024-02-01 10:00:00');

-- Inserimento Supply Points (Dipendono da Customer)
INSERT INTO supply_point (id, code, type, region, city, status, created_at, updated_at, customer_id) VALUES
(2001, 'SP-TEST-001', 'ELECTRICITY', 'Lombardia', 'Milano', 'ACTIVE', '2024-01-15', '2024-01-15', 1001),
(2002, 'SP-TEST-002', 'GAS', 'Lazio', 'Roma', 'ACTIVE', '2024-02-15', '2024-02-15', 1002);

-- Inserimento Invoices (Dipendono da Supply Point e Customer)

-- Fattura 1: Pagata, importo alto, cliente 1001
INSERT INTO invoice (id, invoice_number, issue_date, due_date, payment_date, period_start, period_end, amount, paid_amount, status, supply_point_id, customer_id) VALUES
(3001, 'INV-TEST-001', '2024-03-01', '2024-03-31', '2024-03-25', '2024-02-01', '2024-02-28', 500.00, 500.00, 'PAID', 2001, 1001);

-- Fattura 2: Non pagata, importo basso, cliente 1001
INSERT INTO invoice (id, invoice_number, issue_date, due_date, payment_date, period_start, period_end, amount, paid_amount, status, supply_point_id, customer_id) VALUES
(3002, 'INV-TEST-002', '2024-04-01', '2024-04-30', null, '2024-03-01', '2024-03-31', 150.00, 0.00, 'UNPAID', 2001, 1001);

-- Fattura 3: Emessa ma non ancora scaduta, cliente 1002
INSERT INTO invoice (id, invoice_number, issue_date, due_date, payment_date, period_start, period_end, amount, paid_amount, status, supply_point_id, customer_id) VALUES
(3003, 'INV-TEST-003', '2024-05-01', '2024-05-31', null, '2024-04-01', '2024-04-30', 200.00, 0.00, 'ISSUED', 2002, 1002);