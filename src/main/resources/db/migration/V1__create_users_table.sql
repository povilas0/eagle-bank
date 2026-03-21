CREATE TABLE users (
    id              VARCHAR(255)    NOT NULL PRIMARY KEY,
    name            VARCHAR(255)    NOT NULL,
    email           VARCHAR(255)    NOT NULL,
    phone_number    VARCHAR(20)     NOT NULL,
    address_line1   VARCHAR(255)    NOT NULL,
    address_line2   VARCHAR(255),
    address_line3   VARCHAR(255),
    address_town    VARCHAR(255)    NOT NULL,
    address_county  VARCHAR(255)    NOT NULL,
    address_postcode VARCHAR(20)    NOT NULL,
    created_timestamp TIMESTAMP     NOT NULL,
    updated_timestamp TIMESTAMP     NOT NULL
);
