package com.example.payment.payment.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TossPaymentResponse {

    private String paymentKey;
    private String orderId;
    private String status;
    private int totalAmount;
    private String approvedAt;
}
