package com.example.payment.payment.client.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TossPaymentCancelRequest implements TossPaymentRequest {

    private final String cancelReason;
}
