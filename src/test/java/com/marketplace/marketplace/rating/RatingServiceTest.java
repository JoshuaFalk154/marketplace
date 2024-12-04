package com.marketplace.marketplace.rating;

import com.marketplace.marketplace.DTO.RatingCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.user.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @InjectMocks
    RatingService ratingService;

    @Mock
    RatingRepository ratingRepository;

    @Mock
    ProductService productService;

    @Test
    void createRating_ValidParams_CreatedRating() {
        User user = User.builder()
                .email("user@mail.com")
                .build();

        Product product = Product.builder()
                .productId("someid")
                .title("title1235")
                .description("description is this")
                .price(23.44)
                .quantity(333L)
                .build();

        RatingCreate ratingCreate = RatingCreate.builder()
                .rating(Stars.TWO)
                .productId(product.getProductId())
                .build();

        doReturn(product).when(productService).getProductByProductId(any(String.class));
        doAnswer(invocation -> invocation.getArgument(0)).when(ratingRepository).save(any(Rating.class));

        Rating result = ratingService.createRating(ratingCreate, user);

        assertThat(result).isNotNull();
        assertThat(result.getRating()).isEqualTo(ratingCreate.getRating());
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getProduct()).isEqualTo(product);
    }

//    @ParameterizedTest
//    @MethodSource("createRating_invalidCreateRating_Exception_Data")
//    void createRating_invalidCreateRating_Exception(RatingCreate ratingCreate) {
//        User user = User.builder()
//                .email("user@mail.com")
//                .build();
//
//        assertThatThrownBy(() -> ratingService.createRating(ratingCreate, user))
//                .isInstanceOf(ConstraintViolationException.class);
//    }
//
//    public static Stream<Arguments> createRating_invalidCreateRating_Exception_Data() {
//        return Stream.of(
//                Arguments.of(RatingCreate.builder().rating(null).productId(null).build()),
//                Arguments.of(RatingCreate.builder().rating(Stars.FIVE).productId(" ").build())
//        );
//    }

    @Test
    void createRating_ProductNotExists_Exception() {
        User user = User.builder()
                .email("user@mail.com")
                .build();

        Product product = Product.builder()
                .productId("someid")
                .title("title1235")
                .description("description is this")
                .price(23.44)
                .quantity(333L)
                .build();

        RatingCreate ratingCreate = RatingCreate.builder()
                .rating(Stars.TWO)
                .productId(product.getProductId() + "not")
                .build();

        doThrow(new ResourceNotFoundException("exception"))
                .when(productService).getProductByProductId(ratingCreate.getProductId());

        assertThatThrownBy(() -> ratingService.createRating(ratingCreate, user))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createRating_UserAlreadyRatedProduct_Exception() {
        User user = User.builder()
                .id(11L)
                .email("user@mail.com")
                .build();

        Product product = Product.builder()
                .id(1111L)
                .productId("someid")
                .title("title1235")
                .description("description is this")
                .price(23.44)
                .quantity(333L)
                .build();

        RatingCreate ratingCreate = RatingCreate.builder()
                .rating(Stars.TWO)
                .productId(product.getProductId())
                .build();



        doReturn(Optional.of(new Rating())).when(ratingRepository).findRatingByProduct_idAndUser_id(
                product.getId(),
                user.getId()
        );

        doReturn(product).when(productService).getProductByProductId(product.getProductId());
        assertThatThrownBy(() -> ratingService.createRating(ratingCreate, user))
                .isInstanceOf(InvalidArgumentsException.class);
    }

    @Test
    void deleteRatingByRatingIdAsOwner_AllFine_DeleteRating() {
        User user = User.builder()
                .id(11L)
                .email("user@mail.com")
                .build();

        Product product = Product.builder()
                .id(1111L)
                .productId("someid")
                .title("title1235")
                .description("description is this")
                .price(23.44)
                .quantity(333L)
                .build();

        Rating rating = Rating.builder()
                .ratingId("ratingId")
                .rating(Stars.FIVE)
                .user(user)
                .product(product)
                .build();

        user.setReviews(List.of(rating));
        product.setRatings(List.of(rating));

        RatingService mock = spy(ratingService);
        doReturn(rating).when(mock).findByRatingId(rating.getRatingId());
        doNothing().when(ratingRepository).delete(any(Rating.class));

        mock.deleteRatingByRatingIdAsOwner(rating.getRatingId(), user);

        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    void deleteRatingByRatingIdAsOwner_RatingNotExist_Exception() {
        User user = User.builder()
                .id(11L)
                .email("user@mail.com")
                .build();

        Product product = Product.builder()
                .id(1111L)
                .productId("someid")
                .title("title1235")
                .description("description is this")
                .price(23.44)
                .quantity(333L)
                .build();

        RatingService mock = spy(ratingService);
        doThrow(new ResourceNotFoundException("")).when(mock).findByRatingId("notexistid");

        assertThatThrownBy(() -> mock.deleteRatingByRatingIdAsOwner("notexistid", user))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteRatingByRatingIdAsOwner_NoOwner_Exception() {
        User user = User.builder()
                .id(11L)
                .email("user@mail.com")
                .build();

        User owner = User.builder()
                .id(222L)
                .email("owner@mail.com")
                .build();

        Product product = Product.builder()
                .id(1111L)
                .productId("someid")
                .title("title1235")
                .description("description is this")
                .price(23.44)
                .quantity(333L)
                .build();

        Rating rating = Rating.builder()
                .ratingId("ratingId")
                .rating(Stars.FIVE)
                .user(owner)
                .product(product)
                .build();

        owner.setReviews(List.of(rating));
        product.setRatings(List.of(rating));

        RatingService spyService = spy(ratingService);
        doReturn(rating).when(spyService).findByRatingId(rating.getRatingId());

        assertThatThrownBy(() -> spyService.deleteRatingByRatingIdAsOwner(rating.getRatingId(), user))
                .isInstanceOf(InvalidArgumentsException.class);
    }
    @Test
    void findByRatingId_AllFine_Rating() {
        Rating rating = Rating.builder()
                .ratingId("ratingId")
                .rating(Stars.FIVE)
                .build();

        doReturn(Optional.of(rating)).when(ratingRepository).findRatingByRatingId(rating.getRatingId());
        Rating result = ratingService.findByRatingId(rating.getRatingId());

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(rating);
    }

    @Test
    void findByRatingId_RatingNotExist_Exception() {
        String ratingId = "someIdffajdkfj3344";
        doReturn(Optional.empty()).when(ratingRepository).findRatingByRatingId(ratingId);

        assertThatThrownBy(() -> ratingService.findByRatingId(ratingId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}
