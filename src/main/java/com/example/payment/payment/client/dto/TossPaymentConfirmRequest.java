package com.example.payment.payment.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossPaymentConfirmRequest {

    private final String paymentKey;
    private final String orderId;
    private final int amount;
}
