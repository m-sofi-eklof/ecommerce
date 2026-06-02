package se.sofiekl.userorderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sofiekl.userorderservice.model.Order;
import se.sofiekl.userorderservice.model.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}