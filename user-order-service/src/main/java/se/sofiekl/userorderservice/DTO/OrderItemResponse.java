package se.sofiekl.userorderservice.DTO;

import java.math.BigDecimal;

public record OrderItemResponse(
        Integer productId,
        String title,
        BigDecimal price,
        Integer quantity
) {}