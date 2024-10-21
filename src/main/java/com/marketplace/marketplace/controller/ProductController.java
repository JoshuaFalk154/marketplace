package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.DTO.ProductCreate;
import com.marketplace.marketplace.DTO.ProductResponse;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final Mapper mapper;

    public ResponseEntity<ProductResponse> createProduct(@AuthenticationPrincipal User user, @RequestBody ProductCreate productCreate) {
        Product createdProduct = productService.createProduct(user, productCreate);

        ProductResponse response = mapper.productToProductResponse(createdProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
