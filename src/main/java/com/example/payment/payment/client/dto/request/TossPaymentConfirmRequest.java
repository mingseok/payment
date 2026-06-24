package com.example.payment.payment.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossPaymentConfirmRequest implements TossPaymentRequest {

    private final String paymentKey;
    private final String orderId;
    private final int amount;
}
