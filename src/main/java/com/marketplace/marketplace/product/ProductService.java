package com.marketplace.marketplace.product;

import com.marketplace.marketplace.DTO.ProductCreate;
import com.marketplace.marketplace.DTO.ProductUpdate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ProductAlreadyExists;
import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.marketplace.exceptions.ResourceNotOwnerException;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import jakarta.transaction.Transactional;
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

    public void checkProduct(Product product) {
        checkProductId(product.getProductId());
        checkTitle(product.getTitle());
        checkDescription(product.getDescription());
        checkPrice(product.getPrice());
    }

    public void checkProductId(String productId) {
        if (productId == null || productId.isBlank()) {
            throw new InvalidArgumentsException("product id is invalid");
        }
    }

    public void checkTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidArgumentsException("product title is invalid");
        }
    }

    public void checkDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidArgumentsException("product description is invalid");
        }
    }

    public void checkPrice(Double price) {
        if (price == null || price <= 0) {
            throw new InvalidArgumentsException("product price is invalid");
        }
    }


    public boolean productExists(Product product) {
        return productRepository.findByIdOrProductId(product.getId(), product.getProductId()).isPresent();
    }

    public boolean productExists(String productId) {
        return productRepository.findByProductId(productId).isPresent();
    }

    public List<Product> getProductsWithParams(Optional<Integer> size, Optional<String> title, Optional<Double> maxPrice) {
        int querySize = size.map(s -> Math.min(s, 50)).orElse(50);
        String queryTitle = title.orElse("");

        if (maxPrice.isPresent() && maxPrice.get() > 0) {
            return productRepository.findByTitleContainingAndPriceLessThanEqual(queryTitle, maxPrice.get(), Limit.of(querySize));
        }

        return productRepository.findByTitleContaining(queryTitle, Limit.of(querySize));
    }

    public Product getProductByProductId(String productId) {
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product with id: " + productId + " does not exist"));
    }


    @Transactional
    public void checkOwnership(User user, Product product) {
        User owner = product.getSeller();
        if (!product.getSeller().equals(user)) {
            throw new ResourceNotOwnerException("user with sub " + user.getSub() + " does not own product with id: " + product.getProductId());
        }
    }

    public Product updateProductAsOwner(User user, String id, ProductUpdate update) {
        Product product = getProductByProductId(id);
        checkOwnership(user, product);

        checkTitle(update.getTitle());
        checkDescription(update.getDescription());
        checkPrice(update.getPrice());
        product.setTitle(update.getTitle());
        product.setDescription(update.getDescription());
        product.setPrice(update.getPrice());

        return saveProduct(product);
    }

    @Transactional
    public void deleteProductAsOwner(User user, String productId) {
        Product product = getProductByProductId(productId);
        checkOwnership(user, product);

        productRepository.delete(product);
    }
}
