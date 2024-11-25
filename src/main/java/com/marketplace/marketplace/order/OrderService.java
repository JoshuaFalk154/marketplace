package com.marketplace.marketplace.order;

import com.marketplace.marketplace.DTO.OrderCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.marketplace.orderItem.OrderItem;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.transaction.Transaction;
import com.marketplace.marketplace.transaction.TransactionService;
import com.marketplace.marketplace.transaction.TransactionStatus;
import com.marketplace.marketplace.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final TransactionService transactionService;

    public List<Order> findOrderByOwner_id(Long owner_id) {
        return orderRepository.findOrderByOwner_id(owner_id);
    }

    @Transactional
    public Order createOrder(User user, OrderCreate orderCreate) {
        if (orderCreate.getProductIdQuantityMap().isEmpty()) {
            throw new InvalidArgumentsException("order must contain at least one product");
        }


        List<OrderItem> orderItems = new ArrayList<>();

        for (String productId : orderCreate.getProductIdQuantityMap().keySet()) {
            Product product = productService.getProductByProductId(productId);
            OrderItem orderItem = OrderItem.builder()
                    .quantity(orderCreate.getProductIdQuantityMap().get(productId))
                    .product(product)
                    .build();
            orderItems.add(orderItem);
        }


        Order order = Order.builder()
                .orderId(generateOrderId())
                .status(OrderStatus.PENDING)
                .owner(user)
                .productIdQuantityMap(orderCreate.getProductIdQuantityMap())
                .orderItems(orderItems)
                .build();

        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }

        return orderRepository.save(order);
    }

    public Order getOrderByOrderId(String orderId) {
        return orderRepository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order with id: " + orderId + " does not exist"));
    }

    private String generateOrderId() {
        return UUID.randomUUID().toString().substring(0, 13);
    }

//    @Transactional
//    public void finalizeOrder(String orderId) {
//        Order order = getOrderByOrderId(orderId);
//        for (OrderItem item: order.getOrderItems()) {
//            Product product = item.getProduct();
//            Transaction transaction = order.getTransaction();
//            Long productQuantity = product.getQuantity();
//            Long itemQuantity = item.getQuantity();
//
//            if (productQuantity < itemQuantity) {
//                log.error("order with id {} was placed but product with id {} has not enough stock amount. please handle manually!");
//                transaction.setStatus(TransactionStatus.FAILED);
//                transactionService.save(transaction);
//                return;
//            }
//            product.setQuantity(productQuantity-itemQuantity);
//
//            // TODO
//            // save in a batch
//            productService.saveProduct(product);
//        }
//    }
}
