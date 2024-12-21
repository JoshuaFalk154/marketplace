package com.marketplace.marketplace.product;

import com.marketplace.marketplace.DTO.ProductCreate;
import com.marketplace.marketplace.DTO.ProductUpdate;
import com.marketplace.marketplace.controller.ProductController;
import com.marketplace.marketplace.jwt.CustomAuthenticationToken;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.EntityManagerFactoryAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("mockMvc")
@Testcontainers
public class ProductControllerIntegrationTest {


    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new
            PostgreSQLContainer(DockerImageName.parse("postgres"));


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductController productController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    EntityManager entityManager;

    User user;

    @BeforeEach
    void loadUser() {
        user = User.builder()
                .email("abc@example.com")
                .sub("123ffs")
                .build();
        userRepository.save(user);
        Collection<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_SELLER"));
        AbstractAuthenticationToken token = new CustomAuthenticationToken(user, authorities);
        token.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

//    void loadCustomUser() {
//        User customUser = User.builder()
//                .email("custom@mail.com")
//                .sub("bfbadif")
//                .build();
//
//    }

    @AfterEach
    void resetTables() {
        userRepository.deleteAll();
        productRepository.deleteAll();
    }


    @Test
    void createProduct_AllFine_Response() throws Exception {
        String productId = "1someRandomId123";
        ProductCreate productCreate = ProductCreate.builder()
                .productId(productId)
                .title("titlleee")
                .description("description abc")
                .price(24.2)
                .quantity(100L)
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(productId));

        Product loadedProduct = productRepository.findByProductId(productId).orElse(null);
        assertThat(loadedProduct).isNotNull();
        assertThat(loadedProduct.getSeller()).isNotNull();
        assertThat(loadedProduct.getSeller().getSub()).isEqualTo(user.getSub());
    }

    @Test
    void createProduct_ProductAlreadyExists_BadRequest() throws Exception {
        String productId = "productIdabc123";

        User owner = User.builder()
                .email("owner@example.com")
                .sub("abc312")
                .build();
        userRepository.save(owner);

        Product product = Product.builder()
                .productId(productId)
                .title("title")
                .description("somedescro")
                .price(24.2)
                .quantity(100L)
                .build();
        productRepository.save(product);


        ProductCreate productCreate = ProductCreate.builder()
                .productId(productId)
                .title("titlleee")
                .description("description abc")
                .price(24.2)
                .quantity(100L)
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(productCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_NoSeller_Forbidden() throws Exception {
        AbstractAuthenticationToken token = (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        AbstractAuthenticationToken newToken = new CustomAuthenticationToken(token.getPrincipal(), List.of());
        SecurityContextHolder.getContext().setAuthentication(newToken);

        String productId = "productIdabc123";
        Product product = Product.builder()
                .productId(productId)
                .title("title")
                .description("somedescro")
                .price(24.2)
                .quantity(100L)
                .build();
        productRepository.save(product);


        ProductCreate productCreate = ProductCreate.builder()
                .productId(productId)
                .title("titlleee")
                .description("description abc")
                .price(24.2)
                .quantity(100L)
                .build();

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productCreate))
        ).andExpect(status().isForbidden());
    }

    @Test
    void getProduct_AllFine_Product() throws Exception {
        String productId = "productIdabc123";
        Product product = Product.builder()
                .productId(productId)
                .title("title")
                .description("somedescro")
                .price(24.2)
                .quantity(100L)
                .build();
        productRepository.save(product);

        mockMvc.perform(get("/products/" + product.getProductId())
        ).andExpect(jsonPath("$.productId").value(product.getProductId()));
    }

    @Test
    void getProduct_ProductNotExists_BadRequest() throws Exception {
        String productId = "notexistId";

        mockMvc.perform(get("/products/" + productId))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("updateProduct_AllFine_Product_Data")
    void updateProduct_AllFine_Product(String title, String description, Double price) throws Exception{
        String productId = "productIdabc123";
        Product product = Product.builder()
                .productId(productId)
                .seller(user)
                .title("title")
                .description("somedescro")
                .price(24.2)
                .quantity(100L)
                .build();
        productRepository.save(product);

        ProductUpdate productUpdate = ProductUpdate.builder()
                .title(title)
                .description(description)
                .price(price)
                .build();

        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productUpdate))
        ).andExpect(status().isOk());
    }

    private static Stream<Arguments> updateProduct_AllFine_Product_Data() {
        return Stream.of(
                Arguments.of("title", "description", 22.3),
                Arguments.of("t", "description", 0.2),
                Arguments.of("title", "d  f as", 22.3)
        );
    }

    void updateProduct_InvalidData_BadRequest() {

    }

    void updateProduct_ProductNotExists_BadRequest() {

    }

    void updateProduct_NotOwner_Forbidden() {

    }


}
