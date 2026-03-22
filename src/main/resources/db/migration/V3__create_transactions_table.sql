CREATE TABLE transactions (
    id                VARCHAR(255)    NOT NULL PRIMARY KEY,
    account_number    VARCHAR(8)      NOT NULL,
    amount            DECIMAL(10, 2)  NOT NULL,
    currency          VARCHAR(3)      NOT NULL,
    type              VARCHAR(50)     NOT NULL,
    reference         VARCHAR(255),
    created_timestamp TIMESTAMP       NOT NULL,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
