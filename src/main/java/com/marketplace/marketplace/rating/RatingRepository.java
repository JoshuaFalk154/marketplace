package com.marketplace.marketplace.rating;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findOrderByUser_id(Long user_id);
}


