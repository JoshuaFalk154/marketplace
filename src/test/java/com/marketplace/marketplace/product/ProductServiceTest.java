package com.marketplace.marketplace.product;

import com.marketplace.marketplace.DTO.ProductCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import com.marketplace.marketplace.utils.MapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Spy
    Mapper mapper = new MapperImpl();


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
}
