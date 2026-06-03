package se.sofiekl.userorderservice.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.sofiekl.userorderservice.DTO.OrderRequest;
import se.sofiekl.userorderservice.DTO.OrderResponse;
import se.sofiekl.userorderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal String email,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(email, authHeader, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(orderService.getOrdersForUser(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @AuthenticationPrincipal String email,
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(email, id));
    }
}