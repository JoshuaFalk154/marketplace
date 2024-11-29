package com.marketplace.marketplace.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findOrderByUser_id(Long user_id);
    Optional<Rating> findRatingByRatingId(String ratingId);
    Optional<Rating> findRatingByProduct_idAndUser_id(Long product_id, Long user_id);
}


