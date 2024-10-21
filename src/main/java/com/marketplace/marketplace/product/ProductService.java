package com.marketplace.marketplace.product;

import com.marketplace.marketplace.DTO.ProductCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ProductAlreadyExists;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
