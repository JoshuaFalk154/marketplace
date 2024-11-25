package com.marketplace.marketplace.orderItem;

import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final ProductService productService;

    // throws exception if at least one product does not exist
    @Transactional
    public Double calculatePrice(List<OrderItem> orderItems) {
        double price = 0.0;

        for (OrderItem item: orderItems) {
            Product product = productService.getProductByProductId(item.getProduct().getProductId());
            Long quantity = item.getQuantity();

            if (product.getQuantity() < quantity) {
                throw new ResourceNotFoundException("the product with id: " + product.getProductId() + " does not have a quantity of " + quantity + " items in stock");
            }

            price += (product.getPrice()*quantity);
        }

        return price;
    }

}
