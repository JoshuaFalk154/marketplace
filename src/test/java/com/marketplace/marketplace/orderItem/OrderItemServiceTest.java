package com.marketplace.marketplace.orderItem;

import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {

    @InjectMocks
    OrderItemService orderItemService;

    @Mock
    ProductService productService;

    @Test
    void calculatePrice_ProductNotExist_Exception() {
        Product product1 = Product.builder()
                .productId("product1")
                .price(244.2)
                .quantity(11111L)
                .build();
        Product product2 = Product.builder()
                .productId("product2")
                .price(1.22)
                .quantity(11111L)
                .build();

        OrderItem item1 = OrderItem.builder()
                .product(product1)
                .quantity(112L)
                .build();
        OrderItem item2 = OrderItem.builder()
                .product(product2)
                .quantity(734L)
                .build();

        List<OrderItem> orderItems = List.of(item1, item2);

        doReturn(product1).when(productService).getProductByProductId(product1.getProductId());
        doThrow(new ResourceNotFoundException("not found")).when(productService).getProductByProductId(product2.getProductId());

        assertThatThrownBy(() -> orderItemService.calculatePrice(orderItems))
                .isInstanceOf(ResourceNotFoundException.class);
    }



}
