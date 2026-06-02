package se.sofiekl.userorderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.userorderservice.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}