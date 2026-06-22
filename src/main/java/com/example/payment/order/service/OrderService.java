package com.example.payment.order.service;

import com.example.payment.order.dto.request.OrderCreateRequest;
import com.example.payment.order.dto.response.OrderResponse;
import com.example.payment.order.entity.Order;
import com.example.payment.order.entity.OrderItem;
import com.example.payment.order.repository.OrderItemRepository;
import com.example.payment.order.repository.OrderRepository;
import com.example.payment.product.entity.Product;
import com.example.payment.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        List<Product> products = productRepository.findAllById(request.getProductIds());
        validateAllFound(products, request.getProductIds());

        int totalAmount = products.stream().mapToInt(Product::getPrice).sum();

        Order order = Order.create(request.getMemberId(), totalAmount);
        orderRepository.save(order);

        List<OrderItem> items = saveOrderItems(order.getId(), products);
        return OrderResponse.from(order, items);
    }

    private void validateAllFound(List<Product> products, List<Long> productIds) {
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 상품이 포함되어 있습니다.");
        }
    }

    private List<OrderItem> saveOrderItems(Long orderId, List<Product> products) {
        List<OrderItem> items = products.stream()
                .map(product -> OrderItem.create(orderId, product.getId(),
                        product.getName(), product.getPrice()))
                .toList();
        return orderItemRepository.saveAll(items);
    }
}
