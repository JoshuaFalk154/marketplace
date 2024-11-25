package com.marketplace.marketplace.order;

import com.marketplace.marketplace.DTO.OrderCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.marketplace.exceptions.ResourceNotOwnerException;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.user.User;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductService productService;

    @Test
    void createOrder_NoProducts_Exception() {
        User user = User.builder().email("mail@mail.com").build();
        OrderCreate orderCreate = OrderCreate.builder().productIdQuantityMap(new HashMap<>()).build();

        assertThatThrownBy(() -> orderService.createOrder(user, orderCreate))
                .isInstanceOf(InvalidArgumentsException.class);
    }

    @Test
    void createOrder_ProductNotExists_Exception() {
        User user = User.builder().email("mail@mail.com").build();

        String validProductId = "abjfjf82";
        String invalidProductId = "fjafjdanf";
        Map<String, Long> map = new HashMap<>();
        map.put(validProductId, 100L);
        map.put(invalidProductId, 14L);

        OrderCreate orderCreate = OrderCreate.builder()
                .productIdQuantityMap(map)
                .build();

        doThrow(new ResourceNotOwnerException("")).when(productService).getProductByProductId(invalidProductId);
        doReturn(Product.builder().build()).when(productService).getProductByProductId(validProductId);

        assertThatThrownBy(() -> orderService.createOrder(user, orderCreate))
                .isInstanceOf(ResourceNotOwnerException.class);
    }

    @Test
    void getOrderByOrderId_OrderExists_Order() {
        String orderId = "orderId443";
        Order order = Order.builder()
                .orderId(orderId)
                .status(OrderStatus.PENDING)
                .build();


        doReturn(Optional.of(order)).when(orderRepository).findOrderByOrderId(any(String.class));
        Order result = orderService.getOrderByOrderId(orderId);
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(result.getStatus()).isEqualTo(order.getStatus());

//        assertThatThrownBy(() -> orderService.getOrderByOrderId(orderId))
//                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getOrderByOrderId_OrderNotExists_Exception() {
        String orderId = "orderId443";
        doReturn(Optional.empty()).when(orderRepository).findOrderByOrderId(any(String.class));

        assertThatThrownBy(() -> orderService.getOrderByOrderId(orderId))
                .isInstanceOf(ResourceNotFoundException.class);
    }




}
