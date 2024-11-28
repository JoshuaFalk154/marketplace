package com.marketplace.marketplace.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.marketplace.marketplace.rating.Stars;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingCreate {

    @NotNull
    private Stars rating;

    @NotBlank
    private String productId;

}
