package com.marketplace.marketplace.product;

import com.marketplace.marketplace.DTO.ProductCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ProductAlreadyExists;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final Mapper mapper;

    public List<Product> findProductsBySeller_id(Long seller_id) {
        return productRepository.findProductsBySeller_id(seller_id);
    }

    public Product createProduct(User seller, ProductCreate productCreate) {
        Product product = mapper.productCreateToProduct(productCreate);
        product.setSeller(seller);

        if (productExists(product)) {
            throw new ProductAlreadyExists("product with following id already exists: " + product.getProductId());
        }

        return saveProduct(product);
    }

    public Product saveProduct(Product product) {
        checkProduct(product);
        return productRepository.save(product);
    }

    private void checkProduct(Product product) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            throw new InvalidArgumentsException("product id is invalid");
        }

        if (product.getTitle() == null || product.getTitle().isBlank()) {
            throw new InvalidArgumentsException("product title is invalid");
        }

        if (product.getDescription() == null || product.getDescription().isBlank()) {
            throw new InvalidArgumentsException("product description is invalid");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new InvalidArgumentsException("product price is invalid");
        }

    }

    public boolean productExists(Product product) {
        return productRepository.findByIdOrProductId(product.getId(), product.getProductId()).isPresent();
    }

    public List<Product> getProductsWithParams(Optional<Integer> size, Optional<String> title, Optional<Double> maxPrice) {
        int querySize = size.map(s -> Math.min(s, 50)).orElse(50);
        String queryTitle = title.orElse("");

        if (maxPrice.isPresent() && maxPrice.get() > 0) {
            return productRepository.findByTitleContainingAndPriceLessThan(queryTitle, maxPrice.get(), Limit.of(querySize));
        }

        return productRepository.findByTitleContaining(queryTitle, Limit.of(querySize));
    }
}
