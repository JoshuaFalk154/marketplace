package com.marketplace.marketplace.order;

import com.marketplace.marketplace.DTO.OrderCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public List<Order> findOrderByOwner_id(Long owner_id) {
        return orderRepository.findOrderByOwner_id(owner_id);
    }

    @Transactional
    public Order createOrder(User user, OrderCreate orderCreate) {
        if (orderCreate.getProductIdQuantityMap().isEmpty()) {
            throw new InvalidArgumentsException("order must contain at least one product");
        }

        for (String productId: orderCreate.getProductIdQuantityMap().keySet() ) {
            productService.getProductByProductId(productId);
        }

        return Order.builder()
                .orderId(generateOrderId())
                .status(OrderStatus.PENDING)
                .owner(user)
                .productIdQuantityMap(orderCreate.getProductIdQuantityMap())
                .build();
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString().substring(0, 13);
    }
}
