package com.example.payment.subscription.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 구독 상품 정의. 회원의 실제 가입 기록은 별도 Subscription 엔티티가 담당한다.
@Entity
@Table(name = "subscription_plans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int billingPeriodMonths;

    @Builder
    private SubscriptionPlan(String name, int price, int billingPeriodMonths) {
        this.name = name;
        this.price = price;
        this.billingPeriodMonths = billingPeriodMonths;
    }
}
