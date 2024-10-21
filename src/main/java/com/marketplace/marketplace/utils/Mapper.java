package com.marketplace.marketplace.utils;

import com.marketplace.marketplace.DTO.*;
import com.marketplace.marketplace.order.Order;
import com.marketplace.marketplace.product.Product;
import com.marketplace.marketplace.rating.Rating;
import com.marketplace.marketplace.user.User;
import org.mapstruct.Mapping;

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

    UserResponse userToUserResponse(User user);

    ProductResponse productToProductResponse(Product product);

    Product productCreateToProduct(ProductCreate productCreate);
}
