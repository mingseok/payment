package com.example.payment.payment.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossPaymentCancelRequest {

    private final String cancelReason;
}
