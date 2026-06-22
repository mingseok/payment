package com.example.payment.order.dto.response;

import com.example.payment.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderPaymentConfirmResponse {

    private final String orderId;
    private final String paymentKey;
    private final int amount;
    private final String status;

    public static OrderPaymentConfirmResponse from(String orderId, Payment payment) {
        return OrderPaymentConfirmResponse.builder()
                .orderId(orderId)
                .paymentKey(payment.getPaymentKey())
                .amount(payment.getAmount())
                .status(payment.getStatus().name())
                .build();
    }
}
