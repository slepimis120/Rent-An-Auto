export enum PaymentMethod {
    CARD = 'CARD',
    QR = 'QR',
    PAYPAL = 'PAYPAL',
    CRYPTO = 'CRYPTO'
}
export const paymentMethodNames = {
    [PaymentMethod.CARD]: 'Card',
    [PaymentMethod.QR]: 'IPS QR Code',
    [PaymentMethod.PAYPAL]: 'PayPal',
    [PaymentMethod.CRYPTO]: 'Crypto',
}
