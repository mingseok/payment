package com.example.payment.order.entity;

import com.example.payment.order.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private Order(String orderId, Long memberId, int totalAmount, OrderStatus status,
                  LocalDateTime expiredAt, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }

    public void cancel() {
        validatePending();
        this.status = OrderStatus.CANCELED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
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
}
