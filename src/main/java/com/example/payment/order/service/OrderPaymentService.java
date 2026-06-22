package com.example.payment.order.service;

import com.example.payment.order.dto.request.OrderPaymentConfirmRequest;
import com.example.payment.order.dto.response.OrderPaymentConfirmResponse;
import com.example.payment.order.entity.Order;
import com.example.payment.order.entity.OrderItem;
import com.example.payment.order.repository.OrderItemRepository;
import com.example.payment.payment.client.TossPaymentsClient;
import com.example.payment.payment.client.dto.TossPaymentConfirmRequest;
import com.example.payment.payment.client.dto.TossPaymentResponse;
import com.example.payment.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPaymentService {

    private final OrderItemRepository orderItemRepository;
    private final TossPaymentsClient tossPaymentsClient;
    private final OrderPaymentTransactionService transactionService;

    public OrderPaymentConfirmResponse confirmPayment(String orderId, OrderPaymentConfirmRequest request) {
        Order order = transactionService.markPaymentPending(orderId, request.getAmount());

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        String orderName = generateOrderName(orderItems);

        TossPaymentResponse tossResponse = callTossConfirm(request.getPaymentKey(), orderId, request.getAmount());
        verifyDone(tossResponse, orderId);
        verifyAmount(tossResponse, order.getTotalAmount(), orderId);

        Payment payment = savePayment(order, orderName, tossResponse);
        return OrderPaymentConfirmResponse.from(order.getOrderId(), payment);
    }

    private TossPaymentResponse callTossConfirm(String paymentKey, String orderId, int amount) {
        try {
            return tossPaymentsClient.confirmPayment(
                    new TossPaymentConfirmRequest(paymentKey, orderId, amount));
        } catch (Exception e) {
            transactionService.failOrder(orderId);
            throw new IllegalArgumentException("토스 결제 승인에 실패했습니다.");
        }
    }

    private void verifyDone(TossPaymentResponse response, String orderId) {
        if (!"DONE".equals(response.getStatus())) {
            transactionService.failOrder(orderId);
            throw new IllegalArgumentException("결제가 완료되지 않았습니다.");
        }
    }

    private void verifyAmount(TossPaymentResponse response, int expectedAmount, String orderId) {
        if (response.getTotalAmount() != expectedAmount) {
            transactionService.failOrder(orderId);
            throw new IllegalArgumentException("결제 금액이 주문 금액과 일치하지 않습니다.");
        }
    }

    private Payment savePayment(Order order, String orderName, TossPaymentResponse tossResponse) {
        try {
            PaymentCommand command = PaymentCommand.builder()
                    .orderId(order.getOrderId())
                    .memberId(order.getMemberId())
                    .paymentKey(tossResponse.getPaymentKey())
                    .orderName(orderName)
                    .amount(order.getTotalAmount())
                    .paidAt(parsePaidAt(tossResponse.getApprovedAt()))
                    .build();
            return transactionService.processPayment(command);
        } catch (Exception e) {
            transactionService.failOrder(order.getOrderId());
            throw new IllegalArgumentException("결제 저장에 실패했습니다.");
        }
    }

    private String generateOrderName(List<OrderItem> orderItems) {
        if (orderItems.isEmpty()) {
            return "주문";
        }
        String firstName = firstProductName(orderItems);
        if (orderItems.size() == 1) {
            return firstName;
        }
        return summaryName(firstName, orderItems.size());
    }

    private String summaryName(String firstName, int totalCount) {
        int othersCount = totalCount - 1;
        return String.format("%s 외 %d건", firstName, othersCount);
    }

    private String firstProductName(List<OrderItem> orderItems) {
        return orderItems.get(0).getProductName();
    }

    private LocalDateTime parsePaidAt(String approvedAt) {
        return approvedAt != null
                ? OffsetDateTime.parse(approvedAt).toLocalDateTime()
                : LocalDateTime.now();
    }
}
