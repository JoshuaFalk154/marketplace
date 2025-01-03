package com.marketplace.marketplace.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ProductUpdate {
    private String title;
    private String description;
    private Double price;
}
