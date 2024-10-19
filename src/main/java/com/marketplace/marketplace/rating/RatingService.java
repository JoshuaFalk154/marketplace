package com.marketplace.marketplace.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public List<Rating> findOrderByUser_id(Long owner_id) {
        return ratingRepository.findOrderByUser_id(owner_id);
    }
}
