package com.marketplace.marketplace.product;

import com.marketplace.marketplace.DTO.ProductCreate;
import com.marketplace.marketplace.DTO.ProductUpdate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ProductAlreadyExists;
import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.marketplace.exceptions.ResourceNotOwnerException;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import com.marketplace.marketplace.utils.MapperImpl;
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

        verify(productRepository).findByTitleContainingAndPriceLessThanEqual(
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

    @Test
    void getProductByProductId_ProductExistsAndValidId_Product() {
        String productId = "";
        Product product = Product.builder()
                .productId(productId)
                .title("someTitle")
                .build();

        doReturn(Optional.of(product)).when(productRepository).findByProductId(any(String.class));

        Product result = productService.getProductByProductId(productId);

        assertThat(result).isNotNull();
        assertThat(result.getProductId()).isEqualTo(productId);
        assertThat(result.getTitle()).isEqualTo(product.getTitle());
    }

    @Test
    void getProductByProductId_ProductWithIdNotExist_Exception() {
        String productId = "";
        Exception expectedException = new ResourceNotFoundException("");

        doReturn(Optional.empty()).when(productRepository).findByProductId(any(String.class));

        assertThatThrownBy(() -> productService.getProductByProductId(productId))
                .isInstanceOf(expectedException.getClass());
    }

    @Test
    void updateProductAsOwner_AllValid_UpdatedProduct() {
        String productId = "product1";

        User owner = User.builder().id(1L).sub("sub").email("mail@mail.com").build();
        Product product = Product.builder()
                .id(1L)
                .productId(productId)
                .title("title")
                .description("description")
                .price(29.99)
                .build();

        ProductUpdate productUpdate = ProductUpdate.builder()
                .title("newTitle")
                .description("newDescription")
                .price(300.24)
                .build();

        Product updatedProduct = Product.builder()
                .id(product.getId())
                .productId(product.getProductId())
                .title(productUpdate.getTitle())
                .description(productUpdate.getDescription())
                .price(productUpdate.getPrice())
                .build();

        ProductService productServiceMock = Mockito.spy(productService);
        doReturn(product).when(productServiceMock).getProductByProductId(any(String.class));
        doNothing().when(productServiceMock).checkOwnership(any(User.class), any(Product.class));
        doAnswer(invocation -> invocation.getArgument(0)).when(productServiceMock).saveProduct(any(Product.class));

        Product result = productServiceMock.updateProductAsOwner(owner, product.getProductId(), productUpdate);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(updatedProduct.getTitle());
        assertThat(result.getDescription()).isEqualTo(updatedProduct.getDescription());
        assertThat(result.getPrice()).isEqualTo(updatedProduct.getPrice());
    }

    @Test
    void updateProductAsOwner_InvalidId_Exception() {
        User owner = User.builder().id(1L).sub("sub").email("mail@mail.com").build();

        String productId = "product1";
        Product product = Product.builder()
                .id(1L)
                .productId(productId)
                .title("title")
                .description("description")
                .price(29.99)
                .seller(owner)
                .build();
        owner.setProducts(List.of(product));

        ProductUpdate productUpdate = ProductUpdate.builder()
                .title("newTitle")
                .description("newDescription")
                .price(300.24)
                .build();


        ProductService productServiceMock = Mockito.spy(productService);

        doThrow(new ResourceNotFoundException("")).when(productServiceMock).getProductByProductId(any(String.class));

        assertThatThrownBy(() -> productServiceMock.updateProductAsOwner(owner, "wrongid", productUpdate));

    }

    @Test
    void updateProductAsOwner_NotOwner_Exception() {
        Exception expectedException = new ResourceNotOwnerException("");

        User notOwner = User.builder().id(1L).sub("sub").email("mail@mail.com").build();

        String productId = "product1";
        Product product = Product.builder()
                .id(1L)
                .productId(productId)
                .title("title")
                .description("description")
                .price(29.99)
                .build();

        ProductUpdate productUpdate = ProductUpdate.builder()
                .title("newTitle")
                .description("newDescription")
                .price(300.24)
                .build();

        ProductService productServiceMock = Mockito.spy(productService);

        doReturn(product).when(productServiceMock).getProductByProductId(productId);
        doThrow(new ResourceNotOwnerException("")).when(productServiceMock).checkOwnership(any(User.class), any(Product.class));

        assertThatThrownBy(() -> productServiceMock.updateProductAsOwner(notOwner, productId, productUpdate))
                .isInstanceOf(expectedException.getClass());
    }

    @ParameterizedTest
    @MethodSource("updateProductAsOwner_InvalidProductUpdate_Exception_Values")
    void updateProductAsOwner_InvalidProductUpdate_Exception(String title, String description, Double price) {
        Exception expectedException = new InvalidArgumentsException("");

        User owner = User.builder().id(1L).sub("sub").email("mail@mail.com").build();
        String productId = "product1";

        Product product = Product.builder()
                .id(1L)
                .productId(productId)
                .title("title")
                .description("description")
                .price(29.99)
                .build();

        ProductUpdate productUpdate = ProductUpdate.builder()
                .title(title)
                .description(description)
                .price(price)
                .build();

        ProductService productServiceMock = Mockito.spy(productService);
        doReturn(product).when(productServiceMock).getProductByProductId(any(String.class));
        doNothing().when(productServiceMock).checkOwnership(any(User.class), any(Product.class));

        assertThatThrownBy(() -> productServiceMock.updateProductAsOwner(owner, productId, productUpdate))
                .isInstanceOf(expectedException.getClass());

    }

    public static Stream<Arguments> updateProductAsOwner_InvalidProductUpdate_Exception_Values() {
        return Stream.of(
                Arguments.of(" ", "description", 20.4),
                Arguments.of(null, "description", 20.4),
                Arguments.of("title", " ", 20.4),
                Arguments.of("title", null, 20.4),
                Arguments.of("title", "description", -1.4),
                Arguments.of("title", "description", 0.0),
                Arguments.of("title", "description", null)
        );
    }


}
