package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.DTO.*;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final Mapper mapper;


    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> createProduct(@AuthenticationPrincipal User user, @RequestBody ProductCreate productCreate) {
        Product createdProduct = productService.createProduct(user, productCreate);

        ProductResponse response = mapper.productToProductResponse(createdProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ProductRequested>> getProducts(@RequestParam Optional<Integer> size,
                                                              @RequestParam Optional<String> title,
                                                              @RequestParam Optional<Double> maxPrice) {
        List<Product> products = productService.getProductsWithParams(size, title, maxPrice);
        List<ProductRequested> response = mapper.listProductsToListProductRequested(products);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRequested> getProduct(@PathVariable String id) {
        Product product = productService.getProductByProductId(id);
        ProductRequested response = mapper.productToProductRequested(product);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductUpdated> updateProduct(@AuthenticationPrincipal User user,@PathVariable String id, @RequestBody ProductUpdate update) {
        Product updatedProduct = productService.updateProductAsOwner(user, id, update);
        ProductUpdated result = mapper.productToProductUpdated(updatedProduct);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> deleteProduct(@AuthenticationPrincipal User user, @PathVariable String productId) {
        productService.deleteProductAsOwner(user, productId);

        return new ResponseEntity<>("Deleted product with id: " + productId, HttpStatus.OK);
    }


}
