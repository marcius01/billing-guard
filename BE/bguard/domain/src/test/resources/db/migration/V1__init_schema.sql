CREATE SEQUENCE IF NOT EXISTS anomaly_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS customer_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS import_error_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS import_job_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS invoice_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS supply_point_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE anomaly
(
    id                BIGINT NOT NULL,
    type              VARCHAR(255),
    severity          VARCHAR(255),
    status            VARCHAR(255),
    description       VARCHAR(255),
    technical_details VARCHAR(255),
    resolved_by       VARCHAR(255),
    resolved_at       TIMESTAMP WITHOUT TIME ZONE,
    created_at        TIMESTAMP WITHOUT TIME ZONE,
    invoice_id        BIGINT,
    supply_point_id   BIGINT,
    import_job_id     BIGINT,
    CONSTRAINT pk_anomaly PRIMARY KEY (id)
);

CREATE TABLE customer
(
    id            BIGINT NOT NULL,
    external_code VARCHAR(255),
    name          VARCHAR(255),
    tax_code      VARCHAR(255),
    email         VARCHAR(255),
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE import_error
(
    id            BIGINT NOT NULL,
    row_number    BIGINT NOT NULL,
    field_name    VARCHAR(255),
    raw_value     VARCHAR(255),
    error_code    VARCHAR(255),
    message       VARCHAR(255),
    created_at    date,
    import_job_id BIGINT,
    CONSTRAINT pk_importerror PRIMARY KEY (id)
);

CREATE TABLE import_job
(
    id             BIGINT  NOT NULL,
    filename       VARCHAR(255),
    status         VARCHAR(255),
    total_rows     INTEGER NOT NULL,
    processed_rows INTEGER NOT NULL,
    discarded_rows INTEGER NOT NULL,
    anomaly_rows   INTEGER NOT NULL,
    started_at     TIMESTAMP WITHOUT TIME ZONE,
    completed_at   TIMESTAMP WITHOUT TIME ZONE,
    error_message  VARCHAR(255),
    CONSTRAINT pk_importjob PRIMARY KEY (id)
);

CREATE TABLE invoice
(
    id              BIGINT NOT NULL,
    invoice_number  VARCHAR(255),
    issue_date      date,
    due_date        date,
    payment_date    date,
    period_start    date,
    period_end      date,
    amount          DOUBLE PRECISION,
    paid_amount     DOUBLE PRECISION,
    status          VARCHAR(255),
    supply_point_id BIGINT,
    customer_id     BIGINT,
    CONSTRAINT pk_invoice PRIMARY KEY (id)
);

CREATE TABLE supply_point
(
    id          BIGINT NOT NULL,
    code        VARCHAR(255),
    type        VARCHAR(255),
    region      VARCHAR(255),
    city        VARCHAR(255),
    status      VARCHAR(255),
    created_at  date,
    updated_at  date,
    customer_id BIGINT,
    CONSTRAINT pk_supplypoint PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    role     VARCHAR(255),
    status   VARCHAR(255),
    token    VARCHAR(255),
    email    VARCHAR(255),
    name     VARCHAR(255),
    surname  VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE anomaly
    ADD CONSTRAINT FK_ANOMALY_ON_IMPORTJOB FOREIGN KEY (import_job_id) REFERENCES import_job (id);

ALTER TABLE anomaly
    ADD CONSTRAINT FK_ANOMALY_ON_INVOICE FOREIGN KEY (invoice_id) REFERENCES invoice (id);

ALTER TABLE anomaly
    ADD CONSTRAINT FK_ANOMALY_ON_SUPPLYPOINT FOREIGN KEY (supply_point_id) REFERENCES supply_point (id);

ALTER TABLE import_error
    ADD CONSTRAINT FK_IMPORTERROR_ON_IMPORTJOB FOREIGN KEY (import_job_id) REFERENCES import_job (id);

ALTER TABLE invoice
    ADD CONSTRAINT FK_INVOICE_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE invoice
    ADD CONSTRAINT FK_INVOICE_ON_SUPPLYPOINT FOREIGN KEY (supply_point_id) REFERENCES supply_point (id);

ALTER TABLE supply_point
    ADD CONSTRAINT FK_SUPPLYPOINT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);