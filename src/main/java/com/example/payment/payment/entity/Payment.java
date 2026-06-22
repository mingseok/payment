package com.example.payment.payment.entity;

import com.example.payment.payment.constant.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    // 토스가 발급한 결제 식별자
    @Column(nullable = false, unique = true)
    private String paymentKey;

    // 어느 주문의 결제인가
    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    @Builder
    private Payment(Long memberId, String paymentKey, String orderId, String orderName,
                    int amount, PaymentStatus status, LocalDateTime paidAt) {
        this.memberId = memberId;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.orderName = orderName;
        this.amount = amount;
        this.status = status;
        this.paidAt = paidAt;
    }

    public static Payment createDone(Long memberId, String paymentKey, String orderId,
                                     String orderName, int amount, LocalDateTime paidAt) {
        return Payment.builder()
                .memberId(memberId)
                .paymentKey(paymentKey)
                .orderId(orderId)
                .orderName(orderName)
                .amount(amount)
                .status(PaymentStatus.DONE)
                .paidAt(paidAt)
                .build();
    }

    public static Payment createFailed(Long memberId, String paymentKey, String orderId,
                                       String orderName, int amount) {
        return Payment.builder()
                .memberId(memberId)
                .paymentKey(paymentKey)
                .orderId(orderId)
                .orderName(orderName)
                .amount(amount)
                .status(PaymentStatus.FAILED)
                .build();
    }

    public boolean isDone() {
        return this.status == PaymentStatus.DONE;
    }
}
