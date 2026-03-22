CREATE TABLE accounts (
    account_number    VARCHAR(8)      NOT NULL PRIMARY KEY,
    sort_code         VARCHAR(8)      NOT NULL,
    user_id           VARCHAR(255)    NOT NULL,
    name              VARCHAR(255)    NOT NULL,
    account_type      VARCHAR(50)     NOT NULL,
    balance           DECIMAL(10, 2)  NOT NULL DEFAULT 0.00,
    currency          VARCHAR(3)      NOT NULL,
    created_timestamp TIMESTAMP       NOT NULL,
    updated_timestamp TIMESTAMP       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
