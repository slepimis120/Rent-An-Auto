CREATE DATABASE IF NOT EXISTS `payment-provider`;
CREATE DATABASE IF NOT EXISTS `acquirer-bank`;

GRANT ALL PRIVILEGES ON `payment-provider`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `acquirer-bank`.* TO 'user'@'%';

USE `acquirer-bank`;

CREATE TABLE IF NOT EXISTS merchants (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    merchant_api_key VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS transactions (
    id BINARY(16) PRIMARY KEY,
    stan VARCHAR(255) UNIQUE,
    merchant_id VARCHAR(255) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    psp_timestamp VARCHAR(255),
    acquirer_timestamp VARCHAR(255),
    payment_url VARCHAR(255),
    payment_status VARCHAR(50)
    );

INSERT INTO merchants (id, name, merchant_api_key, is_active)
VALUES (UUID_TO_BIN(UUID()), 'Rent An Auto', 'GqcexvV7aTS0ekMCoULOOovruWz9S3eE', 1);

USE `payment-provider`;

CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) PRIMARY KEY,
    type VARCHAR(31) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    name VARCHAR(255),
    success_url VARCHAR(255),
    failed_url VARCHAR(255),
    error_url VARCHAR(255),
    merchant_api_key VARCHAR(255) UNIQUE
    );

CREATE TABLE IF NOT EXISTS transactions (
    id BINARY(16) PRIMARY KEY,
    merchant_id BINARY(16) NOT NULL,
    merchant_order_id VARCHAR(255) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50),
    merchant_timestamp VARCHAR(255),
    psp_timestamp TIMESTAMP,
    stan VARCHAR(255) UNIQUE,
    external_transaction_id VARCHAR(255),
    payment_url VARCHAR(255),
    FOREIGN KEY (merchant_id) REFERENCES users(id)
    );

CREATE TABLE IF NOT EXISTS merchant_payment_methods (
    merchant_id BINARY(16) NOT NULL,
    enabled_payment_methods VARCHAR(50),
    FOREIGN KEY (merchant_id) REFERENCES users(id)
    );

SET @merchant_id = 'fb440d26-f064-11f0-b818-b262890194ad';

INSERT INTO users (id, type, email, password, role, name, success_url, failed_url, error_url, merchant_api_key)
VALUES (
       UUID_TO_BIN(@merchant_id),
       'MERCHANT',
       'vlasnik@rentacar.com',
       '$2a$10$Vm9HNXN8k2AMjQr.S3G4r.9JArHoUSqKkkCblaZ67q2m440EY8jNq',
       'ROLE_MERCHANT',
       'Rent-A-Car Global',
       'http://localhost:4200/payment/success',
       'http://localhost:4200/payment/failed',
       'http://localhost:4200/payment/error',
       'GqcexvV7aTS0ekMCoULOOovruWz9S3eE'
);

INSERT INTO merchant_payment_methods (merchant_id, enabled_payment_methods)
VALUES
    (UUID_TO_BIN(@merchant_id), 'CARD'),
    (UUID_TO_BIN(@merchant_id), 'QR_CODE'),
    (UUID_TO_BIN(@merchant_id), 'PAYPAL');