package com.marketplace.marketplace.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByOwner_id(Long owner_id);
    Optional<Order> findOrderByOrderId(String orderId);
}
