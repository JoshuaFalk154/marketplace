package com.marketplace.marketplace.product;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsBySeller_id(Long seller_id);

    Optional<Product> findByIdOrProductId(Long id, String productId);

    List<Product> findByTitleContainingAndPriceLessThan(String title, Double maxPrice, Limit limit);
    List<Product> findByTitleContaining(String title, Limit limit);

}
