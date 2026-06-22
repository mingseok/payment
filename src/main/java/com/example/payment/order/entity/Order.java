package com.example.payment.order.entity;

import com.example.payment.order.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 결제 연동 시 토스로 전달
    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private int totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Builder
    private Order(String orderId, Long memberId, int totalAmount, OrderStatus status) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public static Order create(Long memberId, int totalAmount) {
        return Order.builder()
                .orderId(UUID.randomUUID().toString())
                .memberId(memberId)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .build();
    }

    public void cancel() {
        validatePending();
        this.status = OrderStatus.CANCELED;
    }

    public void startPayment(int requestAmount) {
        if (this.status == OrderStatus.PAYMENT_PENDING) {
            throw new IllegalArgumentException("이미 결제가 진행 중인 주문입니다.");
        }
        validatePayable(requestAmount);
        this.status = OrderStatus.PAYMENT_PENDING;
    }

    public void pay() {
        if (this.status != OrderStatus.PAYMENT_PENDING) {
            throw new IllegalArgumentException("결제 선점 상태가 아닙니다.");
        }
        this.status = OrderStatus.PAID;
    }

    public boolean tryFail() {
        if (this.status != OrderStatus.PENDING && this.status != OrderStatus.PAYMENT_PENDING) {
            return false;
        }
        this.status = OrderStatus.FAILED;
        return true;
    }

    public boolean isPending() {
        return this.status == OrderStatus.PENDING;
    }

    public boolean isOwnedBy(Long memberId) {
        return this.memberId.equals(memberId);
    }

    private void validatePending() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalArgumentException("취소할 수 없는 주문 상태입니다.");
        }
    }

    private void validatePayable(int requestAmount) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalArgumentException("결제할 수 없는 주문 상태입니다.");
        }
        if (this.totalAmount != requestAmount) {
            throw new IllegalArgumentException("결제 금액이 주문 금액과 일치하지 않습니다.");
        }
    }
}
