package se.sofiekl.userorderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sofiekl.userorderservice.client.ProductServiceClient;
import se.sofiekl.userorderservice.DTO.*;
import se.sofiekl.userorderservice.model.Order;
import se.sofiekl.userorderservice.model.OrderItem;
import se.sofiekl.userorderservice.model.User;
import se.sofiekl.userorderservice.repository.OrderRepository;
import se.sofiekl.userorderservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductServiceClient productServiceClient;

    @Transactional
    public OrderResponse createOrder(String email, String authHeader, OrderRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = Order.builder()
                .user(user)
                .build();

        List<OrderItem> items = request.getItems().stream()
                .map(itemRequest -> {
                    se.sofiekl.userorderservice.dto.ProductResponse product = productServiceClient
                            .getProductById(itemRequest.getProductId(), authHeader);

                    return OrderItem.builder()
                            .order(order)
                            .productId(itemRequest.getProductId())
                            .title(product.getTitle())
                            .price(product.getPrice())
                            .quantity(itemRequest.getQuantity())
                            .build();
                })
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