package com.marketplace.marketplace.DTO;

import com.marketplace.marketplace.order.Order;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.rating.Rating;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequested {


    private String sub;

    private String email;

    private String phoneNumber;

    private List<OrderNested> orders;

    private List<RatingNested> reviews;

    private List<ProductNested> products;

    private Date createdAt;

    private Date updatedAt;

}
