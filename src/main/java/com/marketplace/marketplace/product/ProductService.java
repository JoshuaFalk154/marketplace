package com.marketplace.marketplace.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findProductsBySeller_id(Long seller_id) {
        return productRepository.findProductsBySeller_id(seller_id);
    }
}
