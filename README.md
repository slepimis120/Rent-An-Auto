# Rent an Auto

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Django](https://img.shields.io/badge/django-%23092E20.svg?style=for-the-badge&logo=django&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

***
The system simulates a real-world **car rental platform integrated with a Payment Service Provider (PSP)**, focusing on electronic payment flows, banking communication, and multiple payment methods.
***

## Setup

### Requirements
- Docker

### Installation

1. Fill in the environment variables in the .env.local file based on the .example.env.local file.

2. Start the Docker instance:
   ```sh
   docker-compose up --build
   ```


## System Architecture

The system is composed of three main parts:

1. Web Shop (Car Rental System)
    - The web shop allows users to browse vehicles, create reservations, and pay for rental services through the PSP.
2. Payment Service Provider (PSP)
    - The PSP acts as a middle layer between the web shop and payment services. It manages transactions and provides available payment options to users.
3. Banking System
    - The banking system processes card and QR payments, validates transaction data, and returns payment results to the PSP.


## Supported Payment Methods

1. Card Payment
    - Simulates a standard online card payment flow between the Web Shop, PSP, and Banking System. Users enter their card details on the bank's payment page, after which the transaction is validated and processed.

2. QR Payment
    - Simulates an IPS QR payment flow. A QR code is generated for the transaction, allowing the user to complete the payment by scanning it. Once processed, the transaction status is sent back through the PSP to the Web Shop.


## License
This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.