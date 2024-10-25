package com.marketplace.marketplace.product;

import com.marketplace.marketplace.DTO.ProductCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ProductAlreadyExists;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import com.marketplace.marketplace.utils.MapperImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Limit;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Spy
    Mapper mapper = new MapperImpl();

    @Test
    void ProductServiceCreateProduct_ValidProduct_CreatedProduct() {
        User seller = User.builder()
                .id(1L)
                .sub("sub")
                .email("email@mail.com")
                .createdAt(Date.from(Instant.now()))
                .updatedAt(Date.from(Instant.now()))
                .build();

        ProductCreate productCreate = ProductCreate.builder()
                .productId("productId")
                .title("title")
                .description("description")
                .price(28.0)
                .build();

        ProductService productServiceMock = Mockito.spy(productService);
        doReturn(false).when(productServiceMock).productExists(any(Product.class));
        doAnswer(invocation -> invocation.getArgument(0)).when(productServiceMock).saveProduct(any(Product.class));

        Product result = productServiceMock.createProduct(seller, productCreate);

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productCreate.getProductId());
        assertThat(result.getTitle()).isEqualTo(productCreate.getTitle());
        assertThat(result.getPrice()).isEqualTo(productCreate.getPrice());
    }

    @ParameterizedTest
    @MethodSource("ProductServiceCreateProduct_InvalidProduct_Exception_data")
    void ProductServiceCreateProduct_InvalidProduct_Exception(String productId, String title, String description, Double price) {
        User seller = User.builder()
                .id(1L)
                .sub("sub")
                .email("email@mail.com")
                .createdAt(Date.from(Instant.now()))
                .updatedAt(Date.from(Instant.now()))
                .build();

        ProductCreate productCreate = ProductCreate.builder()
                .productId(productId)
                .title(title)
                .description(description)
                .price(price)
                .build();

        assertThatThrownBy(() -> productService.createProduct(seller, productCreate))
                .isInstanceOf(InvalidArgumentsException.class);

    }

    public static Stream<Arguments> ProductServiceCreateProduct_InvalidProduct_Exception_data() {
        return Stream.of(
                Arguments.of(" ", "title", "description", 28.2),
                Arguments.of(null, "title", "description", 28.2),
                Arguments.of("validId", null, "description", 28.2),
                Arguments.of("validId", "  ", "description", 28.2),
                Arguments.of("validId", "title", null, 28.2),
                Arguments.of("validId", "title", " ", 28.2),
                Arguments.of("validId", "title", "description", null),
                Arguments.of("validId", "title", "description", 0.0),
                Arguments.of("validId", "title", "description", -1.0)
        );
    }

    @Test
    void ProductServiceCreateProduct_ProductExists_Exception() {
        User seller = User.builder()
                .id(1L)
                .sub("sub")
                .email("email@mail.com")
                .createdAt(Date.from(Instant.now()))
                .updatedAt(Date.from(Instant.now()))
                .build();

        Product existingProduct = Product.builder()
                .id(1L)
                .productId("productId")
                .title("title")
                .seller(seller)
                .build();
        seller.setProducts(List.of(existingProduct));

        ProductCreate createProduct = ProductCreate.builder()
                .productId("productId")
                .title("title")
                .description("description")
                .build();

        ProductService productServiceMock = Mockito.spy(productService);
        doReturn(true).when(productServiceMock).productExists(any(Product.class));

        assertThatThrownBy(() -> productServiceMock.createProduct(seller, createProduct))
                .isInstanceOf(ProductAlreadyExists.class);
    }

    @ParameterizedTest
    @MethodSource("ProductServiceGetProductsWithParams_WithMaxPrice_CallsCorrectRepositoryMethod_Values")
    void ProductServiceGetProductsWithParams_WithMaxPrice_CallsCorrectRepositoryMethod(Optional<Integer> size, Optional<String> title, Optional<Double> maxPrice,
                                                                                       int wantSize, String wantTitle, Double wantPrize) {

        productService.getProductsWithParams(size, title, maxPrice);

        verify(productRepository).findByTitleContainingAndPriceLessThan(
                eq(wantTitle), eq(wantPrize), eq(Limit.of(wantSize))
        );
        verifyNoMoreInteractions(productRepository);
    }

    public static Stream<Arguments> ProductServiceGetProductsWithParams_WithMaxPrice_CallsCorrectRepositoryMethod_Values() {
        return Stream.of(
                Arguments.of(Optional.of(45), Optional.of("title"), Optional.of(150.0),
                        45, "title", 150.0),
                Arguments.of(Optional.of(125), Optional.of("title"), Optional.of(1.0),
                        50, "title", 1.0),
                Arguments.of(Optional.empty(), Optional.of("title"), Optional.of(1.0),
                        50, "title", 1.0),

                Arguments.of(Optional.of(45), Optional.empty(), Optional.of(1.0),
                        45, "", 1.0),
                Arguments.of(Optional.of(45), Optional.of("title"), Optional.of(150.0),
                        45, "title", 150.0)
        );
    }

    @ParameterizedTest
    @MethodSource("ProductServiceGetProductsWithParams_InvalidOrEmptyMaxPrice_List_Values")
    void ProductServiceGetProductsWithParams_InvalidOrEmptyMaxPrice_List(Optional<Integer> size, Optional<String> title, Optional<Double> maxPrice,
                                                                         int wantSize, String wantTitle) {

        productService.getProductsWithParams(size, title, maxPrice);

        verify(productRepository).findByTitleContaining(
                eq(wantTitle), eq(Limit.of(wantSize))
        );
        verifyNoMoreInteractions(productRepository);
    }

    public static Stream<Arguments> ProductServiceGetProductsWithParams_InvalidOrEmptyMaxPrice_List_Values() {
        return Stream.of(
                Arguments.of(Optional.of(125), Optional.of("title"), Optional.empty(),
                        50, "title", 1.0),
                Arguments.of(Optional.of(125), Optional.of("title"), Optional.of(-1.0),
                        50, "title", 1.0)
        );
    }


}
