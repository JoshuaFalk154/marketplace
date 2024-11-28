package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.DTO.RatingCreate;
import com.marketplace.marketplace.DTO.RatingResponse;
import com.marketplace.marketplace.rating.Rating;
import com.marketplace.marketplace.rating.RatingService;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ratings")
@Validated
public class RatingController {

    private final RatingService ratingService;
    private final Mapper mapper;

    @PostMapping
    public ResponseEntity<RatingResponse> createRating(@RequestBody @Validated RatingCreate ratingCreate, @AuthenticationPrincipal User user) {
        Rating rating = ratingService.createRating(ratingCreate, user);
        RatingResponse response = mapper.ratingToRatingResponse(rating);
        return  new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable String id) {
        ratingService.deleteByRatingId(id);
        return new ResponseEntity<>("Sucessfully deleted rating with id "+id, HttpStatus.OK);
    }

}
