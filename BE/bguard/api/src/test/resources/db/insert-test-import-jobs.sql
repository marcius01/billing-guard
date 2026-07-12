-- Inserimento Import Jobs

-- Job 1: Completato senza errori
INSERT INTO import_job (id, filename, status, total_rows, processed_rows, discarded_rows, anomaly_rows, started_at, completed_at, error_message) VALUES
(4001, 'invoices-batch-2025-01.csv', 'COMPLETED', 5, 5, 0, 0, '2025-01-10 09:00:00', '2025-01-10 09:00:12', null);

-- Job 2: Completato con errori e un'anomalia rilevata
INSERT INTO import_job (id, filename, status, total_rows, processed_rows, discarded_rows, anomaly_rows, started_at, completed_at, error_message) VALUES
(4002, 'invoices-batch-2025-02.csv', 'COMPLETED_WITH_ERRORS', 6, 4, 2, 1, '2025-02-10 09:00:00', '2025-02-10 09:00:18', null);

-- Job 3: Fallito per errore bloccante (es. header non valido)
INSERT INTO import_job (id, filename, status, total_rows, processed_rows, discarded_rows, anomaly_rows, started_at, completed_at, error_message) VALUES
(4003, 'invoices-batch-2025-03.csv', 'FAILED', 0, 0, 0, 0, '2025-03-10 09:00:00', '2025-03-10 09:00:02', 'Header is missing required fields [INVOICENUMBER]');

-- Import Errors (Dipendono da Import Job)

-- Errore 1: riga 3 del job 4002, invoiceNumber mancante
INSERT INTO import_error (id, row_number, field_name, raw_value, error_code, message, created_at, import_job_id) VALUES
(5001, 3, 'invoiceNumber', null, null, 'required', '2025-02-10', 4002);

-- Errore 2: riga 5 del job 4002, amount non numerico
INSERT INTO import_error (id, row_number, field_name, raw_value, error_code, message, created_at, import_job_id) VALUES
(5002, 5, 'amount', 'ABC', null, 'wrong data format for value ABC', '2025-02-10', 4002);