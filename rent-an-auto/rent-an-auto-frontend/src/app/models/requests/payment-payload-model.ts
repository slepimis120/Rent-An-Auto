export interface PaymentPayloadDTO {
    paymentId: string;
    orderId: string;
    acquirer_order_id: string;
    acquirer_timestamp: number;
    amount: number;
    issuer_order_id: string;
    issuer_timestamp: number;
}

export interface PaymentRequestDTO {
    status: string;
    orderId: string;
    payload: PaymentPayloadDTO | string;
}
