package com.example.payment.order.controller;

import com.example.payment.order.dto.request.OrderCreateRequest;
import com.example.payment.order.dto.request.OrderPaymentConfirmRequest;
import com.example.payment.order.dto.response.OrderPaymentConfirmResponse;
import com.example.payment.order.dto.response.OrderResponse;
import com.example.payment.order.service.OrderPaymentService;
import com.example.payment.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderPaymentService orderPaymentService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderPaymentConfirmResponse> confirm(
            @PathVariable String orderId,
            @Valid @RequestBody OrderPaymentConfirmRequest request) {
        OrderPaymentConfirmResponse response = orderPaymentService.confirmPayment(orderId, request);
        return ResponseEntity.ok(response);
    }
}
