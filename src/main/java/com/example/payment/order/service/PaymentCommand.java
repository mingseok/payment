package com.example.payment.order.service;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentCommand {

    private final String orderId;
    private final Long memberId;
    private final String paymentKey;
    private final String orderName;
    private final int amount;
    private final LocalDateTime paidAt;
}
