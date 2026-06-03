package se.sofiekl.userorderservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sofiekl.userorderservice.client.ProductServiceClient;
import se.sofiekl.userorderservice.DTO.*;
import se.sofiekl.userorderservice.model.Order;
import se.sofiekl.userorderservice.model.OrderItem;
import se.sofiekl.userorderservice.model.User;
import se.sofiekl.userorderservice.repository.OrderRepository;
import se.sofiekl.userorderservice.repository.UserRepository;
import se.sofiekl.userorderservice.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductServiceClient productServiceClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_success() {
        User user = User.builder()
                .id(1L)
                .email("sofia@example.com")
                .username("sofia")
                .build();

        ProductResponse product = new ProductResponse();
        product.setId(1);
        product.setTitle("Fjallraven Backpack");
        product.setPrice(BigDecimal.valueOf(109.95));

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(1);
        itemRequest.setQuantity(2);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(List.of(itemRequest));

        Order savedOrder = Order.builder()
                .id(1L)
                .user(user)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .items(List.of(OrderItem.builder()
                        .productId(1)
                        .title("Fjallraven Backpack")
                        .price(BigDecimal.valueOf(109.95))
                        .quantity(2)
                        .build()))
                .build();

        when(userRepository.findByEmail("sofia@example.com")).thenReturn(Optional.of(user));
        when(productServiceClient.getProductById(1, "Bearer token")).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        OrderResponse response = orderService.createOrder("sofia@example.com", "Bearer token", orderRequest);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("PENDING", response.status());
        assertEquals(1, response.items().size());
        assertEquals("Fjallraven Backpack", response.items().get(0).title());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_userNotFound_throwsException() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItems(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.createOrder("nobody@example.com", "Bearer token", orderRequest));

        assertEquals("User not found", ex.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrdersForUser_success() {
        User user = User.builder()
                .id(1L)
                .email("sofia@example.com")
                .build();

        Order order = Order.builder()
                .id(1L)
                .user(user)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .items(List.of())
                .build();

        when(userRepository.findByEmail("sofia@example.com")).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getOrdersForUser("sofia@example.com");

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).id());
    }
}