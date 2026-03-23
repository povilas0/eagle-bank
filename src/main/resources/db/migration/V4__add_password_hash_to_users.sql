ALTER TABLE users ADD COLUMN password_hash VARCHAR(255) NOT NULL DEFAULT '';
ALTER TABLE users ADD CONSTRAINT users_email_unique UNIQUE (email);
