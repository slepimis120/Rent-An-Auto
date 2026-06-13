CREATE DATABASE IF NOT EXISTS `payment-provider`;
CREATE DATABASE IF NOT EXISTS `acquirer-bank`;
CREATE DATABASE IF NOT EXISTS `rentacar`;

GRANT ALL PRIVILEGES ON `payment-provider`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `acquirer-bank`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `rentacar`.* TO 'user'@'%';

USE `acquirer-bank`;

CREATE TABLE IF NOT EXISTS merchants (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    merchant_api_key VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    account_number VARCHAR(34) NOT NULL
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

CREATE TABLE IF NOT EXISTS card_holders (
    id BINARY(16) PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    pan_encrypted VARCHAR(64) NOT NULL UNIQUE,
    expiry_date VARCHAR(10) NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

INSERT INTO merchants (
    id,
    name,
    merchant_api_key,
    is_active,
    account_number
)
VALUES (
    UUID_TO_BIN(UUID()),
    'Rent An Auto',
    'GqcexvV7aTS0ekMCoULOOovruWz9S3eE',
    1,
    '840000000095584510'
);

INSERT INTO card_holders (
    id,
    full_name,
    pan_encrypted,
    expiry_date,
    balance,
    active
)
VALUES (
    UUID_TO_BIN(UUID()),
    'Petar Petrovic',
    'f0473703743fdad62dea9f70437c158691bcdbf67e443403b8764520274bca3f',
    '12/30',
    1000000.00,
    1
),
(
    UUID_TO_BIN(UUID()),
    'Marko Markovic',
    '8a66f48b3589dfada2d2895ea9d1e858c9baa1e64f81f98f156e761493052327',
    '10/29',
    5.00,
    1
);
-- Broj Kartice Petra: 5186001700008785, CV: 787
-- Broj Kartice Marka: 5186001700009726, CV: 998

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
    merchant_api_key VARCHAR(255) UNIQUE,
    account_number VARCHAR(34) NOT NULL
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

INSERT INTO users (id, type, email, password, role, name, success_url, failed_url, error_url, merchant_api_key, account_number)
VALUES (
        UUID_TO_BIN(@merchant_id),
        'MERCHANT',
        'vlasnik@rentacar.com',
        '$2a$10$Vm9HNXN8k2AMjQr.S3G4r.9JArHoUSqKkkCblaZ67q2m440EY8jNq',
        'ROLE_MERCHANT',
        'Rent-A-Car Global',
        'http://localhost:4301/payment/success',
        'http://localhost:4301/payment/failed',
        'http://localhost:4301/payment/error',
        'GqcexvV7aTS0ekMCoULOOovruWz9S3eE',
        '840000000095584510'
);

INSERT INTO merchant_payment_methods (merchant_id, enabled_payment_methods)
VALUES
    (UUID_TO_BIN(@merchant_id), 'CARD'),
    (UUID_TO_BIN(@merchant_id), 'QR_CODE'),
    (UUID_TO_BIN(@merchant_id), 'PAYPAL');