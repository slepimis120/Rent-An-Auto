CREATE DATABASE IF NOT EXISTS `payment-provider`;
CREATE DATABASE IF NOT EXISTS `acquirer-bank`;

GRANT ALL PRIVILEGES ON `payment-provider`.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON `acquirer-bank`.* TO 'user'@'%';