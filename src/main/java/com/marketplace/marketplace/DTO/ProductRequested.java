package com.marketplace.marketplace.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequested {
    private String productId;
    private String title;
    private String description;
    private Double price;
}
