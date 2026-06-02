package se.sofiekl.userorderservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sofiekl.userorderservice.DTO.*;
import se.sofiekl.userorderservice.model.Order;
import se.sofiekl.userorderservice.model.OrderItem;
import se.sofiekl.userorderservice.model.User;
import se.sofiekl.userorderservice.repository.OrderRepository;
import se.sofiekl.userorderservice.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderResponse createOrder(String email, OrderRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = Order.builder()
                .user(user)
                .build();

        List<OrderItem> items = request.getItems().stream()
                .map(itemRequest -> OrderItem.builder()
                        .order(order)
                        .productId(itemRequest.getProductId())
                        .title("Product " + itemRequest.getProductId())
                        .price(BigDecimal.ZERO)
                        .quantity(itemRequest.getQuantity())
                        .build())
                .toList();

        order.setItems(items);
        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Transactional
    public List<OrderResponse> getOrdersForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public OrderResponse getOrderById(String email, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProductId(),
                        item.getTitle(),
                        item.getPrice(),
                        item.getQuantity()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getCreatedAt(),
                itemResponses);
    }
}