CREATE TABLE promotion_codes (
     id UUID PRIMARY KEY,
     code VARCHAR(100) NOT NULL UNIQUE,
     discount_percentage DOUBLE PRECISION NOT NULL,
     expiration_date DATE,
     active BOOLEAN NOT NULL DEFAULT TRUE
);

