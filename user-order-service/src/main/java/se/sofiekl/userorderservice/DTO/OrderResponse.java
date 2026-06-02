package se.sofiekl.userorderservice.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {}