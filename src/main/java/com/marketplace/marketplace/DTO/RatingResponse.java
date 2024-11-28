package com.marketplace.marketplace.DTO;

import com.marketplace.marketplace.rating.Stars;
import lombok.*;
import org.hibernate.sql.ast.tree.expression.Star;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
    private String ratingId;
    private Stars rating;
}
