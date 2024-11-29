package com.marketplace.marketplace.rating;

import com.marketplace.marketplace.DTO.RatingCreate;
import com.marketplace.marketplace.exceptions.InvalidArgumentsException;
import com.marketplace.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.product.ProductRepository;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Utils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ProductService productService;

    public List<Rating> findOrderByUser_id(Long owner_id) {
        return ratingRepository.findOrderByUser_id(owner_id);
    }

    @Transactional
    public Rating createRating(@Validated RatingCreate ratingCreate, User user) {
        Product product = productService.getProductByProductId(ratingCreate.getProductId());
        ratingRepository.findRatingByProduct_idAndUser_id(product.getId(), user.getId())
                .ifPresent(r ->
                        {throw  new InvalidArgumentsException("User with id + " + user.getEmail() +
                                " has already rated product with id " + product.getProductId());});

        Rating rating = Rating.builder()
                .ratingId(createRatingId())
                .rating(ratingCreate.getRating())
                .user(user)
                .product(product)
                .build();
        return ratingRepository.save(rating);
    }

    public void deleteByRatingId(String id) {
        Rating rating = ratingRepository.findRatingByRatingId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating with id " + id + " not found."));

        ratingRepository.delete(rating);
    }

    private String createRatingId() {
        return UUID.randomUUID().toString().substring(0, 13);
    }
}
