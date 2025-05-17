-- Crear tipo ENUM para estados de pago (asumiendo que paymentStatus no existe)
CREATE TYPE paymentStatus AS ENUM (
    'PAGADO',
    'PENDIENTE'
    );

-- Crear tipo ENUM para estados de cuenta
CREATE TYPE accountStatus AS ENUM (
    'ACTIVA',
    'BLOQUEDA',
    'CANCELADA'
    );

-- Tabla debit_accounts
CREATE TABLE debit_accounts
(
    client VARCHAR(10)    NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    status accountStatus  NOT NULL,
    -- Constraints
    CONSTRAINT pk_debit_accounts PRIMARY KEY (client),
    -- Índices
    CONSTRAINT ck_positive_amount CHECK (amount >= 0)
);

CREATE TABLE loans
(
    id               INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client           VARCHAR(10)    NOT NULL,
    transaction_date DATE           NOT NULL,
    amount           NUMERIC(19, 2) NOT NULL,
    payment_status   paymentStatus  NOT NULL,
    -- Constraints
    CONSTRAINT fk_loans_client
        FOREIGN KEY (client)
            REFERENCES debit_accounts (client)
            ON DELETE RESTRICT
            ON UPDATE CASCADE,
    CONSTRAINT ck_positive_loan_amount CHECK (amount > 0)
);

-- Índices para debit_accounts
CREATE INDEX idx_debit_accounts_status ON debit_accounts (status);
CREATE INDEX idx_debit_accounts_amount ON debit_accounts (amount);
CREATE INDEX idx_debit_accounts_status_amount ON debit_accounts (status, amount);

-- Índices para loans
CREATE INDEX idx_loans_client_status_date ON loans (client, payment_status, transaction_date DESC);
CREATE INDEX idx_loans_transaction_date ON loans (transaction_date DESC);
CREATE INDEX idx_loans_amount ON loans (amount);
CREATE INDEX idx_loans_active_status ON loans (client) WHERE payment_status = 'PENDIENTE';

insert into debit_accounts values ('00103228', 15375.28, 'ACTIVA');
insert into debit_accounts values ('70099925', 3728.51, 'BLOQUEDA');
insert into debit_accounts values ('00298185', 0.00, 'CANCELADA');
insert into debit_accounts values ('15000125', 235.28, 'ACTIVA');

insert into loans (client, transaction_date, amount, payment_status) values ('00103228', '2021-01-10', 37500.00, 'PENDIENTE');
insert into loans (client, transaction_date, amount, payment_status) values ('00103228', '2021-01-19', 725.18, 'PENDIENTE');
insert into loans (client, transaction_date, amount, payment_status) values ('00103228', '2021-01-31', 1578.22, 'PENDIENTE');
insert into loans (client, transaction_date, amount, payment_status) values ('00103228', '2021-02-04', 380.00, 'PENDIENTE');
insert into loans (client, transaction_date, amount, payment_status) values ('70099925', '2021-01-07', 2175.25, 'PAGADO');
insert into loans (client, transaction_date, amount, payment_status) values ('70099925', '2021-01-15', 499.99, 'PAGADO');
insert into loans (client, transaction_date, amount, payment_status) values ('70099925', '2021-01-24', 5725.18, 'PENDIENTE');
insert into loans (client, transaction_date, amount, payment_status) values ('70099925', '2021-02-07', 876.13, 'PENDIENTE');
insert into loans (client, transaction_date, amount, payment_status) values ('00298185', '2021-02-04', 545.55, 'PENDIENTE');
insert into loans (client, transaction_date, amount, payment_status) values ('15000125', '2020-12-31', 15220.00, 'PAGADO');