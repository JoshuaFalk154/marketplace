package com.marketplace.marketplace.utils;

import com.marketplace.marketplace.DTO.OrderNested;
import com.marketplace.marketplace.DTO.ProductNested;
import com.marketplace.marketplace.DTO.RatingNested;
import com.marketplace.marketplace.DTO.UserRequested;
import com.marketplace.marketplace.order.Order;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.rating.Rating;
import com.marketplace.marketplace.user.User;

@org.mapstruct.Mapper
public interface Mapper {
    UserRequested userToUserRequested(User user);
    User userRequestedToUser(UserRequested userRequested);

    OrderNested orderToOrderNested(Order order);
    Order orderToOrderTested(OrderNested orderNested);

    RatingNested ratingToRatingNested(Rating rating);
    Rating ratingNestedToRating(RatingNested ratingNested);

    ProductNested productToProductNested(Product product);
    Product productNestedToProduct(ProductNested productNested);

}
