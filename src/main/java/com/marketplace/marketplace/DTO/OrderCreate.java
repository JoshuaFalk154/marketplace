package com.marketplace.marketplace.DTO;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreate {
    private Map<String, Long> productIdQuantityMap;
}
