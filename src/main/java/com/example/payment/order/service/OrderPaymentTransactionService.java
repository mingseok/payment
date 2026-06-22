package com.example.payment.order.service;

import com.example.payment.order.entity.Order;
import com.example.payment.order.repository.OrderRepository;
import com.example.payment.payment.entity.Payment;
import com.example.payment.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderPaymentTransactionService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Order markPaymentPending(String orderId, int requestAmount) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        order.startPayment(requestAmount);
        return order;
    }

    @Transactional
    public Payment processPayment(PaymentCommand command) {
        Order order = orderRepository.findByOrderId(command.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        Payment payment = Payment.createDone(
                command.getMemberId(), command.getPaymentKey(), command.getOrderId(),
                command.getOrderName(), command.getAmount(), command.getPaidAt());
        paymentRepository.save(payment);

        order.pay();
        return payment;
    }

    @Transactional
    public void failOrder(String orderId) {
        orderRepository.findByOrderId(orderId).ifPresent(Order::tryFail);
    }
}
