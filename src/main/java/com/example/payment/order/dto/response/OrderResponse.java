package com.example.payment.order.dto.response;

import com.example.payment.order.constant.OrderStatus;
import com.example.payment.order.entity.Order;
import com.example.payment.order.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderResponse {

    private final String orderId;
    private final int totalAmount;
    private final OrderStatus status;
    private final List<OrderItemResponse> items;

    public static OrderResponse from(Order order, List<OrderItem> items) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(items.stream().map(OrderItemResponse::from).toList())
                .build();
    }
}
