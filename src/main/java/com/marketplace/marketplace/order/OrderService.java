package com.marketplace.marketplace.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> findOrderByOwner_id(Long owner_id) {
        return orderRepository.findOrderByOwner_id(owner_id);
    }
}
