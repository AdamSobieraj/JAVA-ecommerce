CREATE TABLE promotion_codes (
     id BIGINT PRIMARY KEY,
     code VARCHAR(100) NOT NULL UNIQUE,
     discount_percentage DOUBLE NOT NULL,
     expiration_date DATE,
     active BOOLEAN NOT NULL DEFAULT TRUE
);

