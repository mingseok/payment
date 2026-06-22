package com.example.payment.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int price;

    @Builder
    private OrderItem(Long orderId, Long productId, String productName, int price) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public static OrderItem create(Long orderId, Long productId, String productName, int price) {
        return OrderItem.builder()
                .orderId(orderId)
                .productId(productId)
                .productName(productName)
                .price(price)
                .build();
    }
}
