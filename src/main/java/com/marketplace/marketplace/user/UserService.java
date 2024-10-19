package com.marketplace.marketplace.user;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.marketplace.marketplace.DTO.UpdateUser;
import com.marketplace.marketplace.exceptions.UserInvalidArgumentsException;
import com.marketplace.marketplace.order.OrderService;
import com.marketplace.marketplace.product.ProductService;
import com.marketplace.marketplace.rating.RatingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final OrderService orderService;
    private final RatingService ratingService;
    private final ProductService productService;

    public Optional<User> findBySub(String sub) {
        return userRepository.findBySub(sub);
    }

    public void loadUserEntities(User user) {
        user.setOrders(orderService.findOrderByOwner_id(user.getId()));
        user.setReviews(ratingService.findOrderByUser_id(user.getId()));
        user.setProducts(productService.findProductsBySeller_id(user.getId()));
    }

    public User saveUser(User user) {
        User checkedUser = checkUser(user);
        return userRepository.save(checkedUser);
    }

    private User checkUser(User user) {
        if (user.getSub() == null || user.getSub().isEmpty() || user.getSub().isBlank()) {
            throw new UserInvalidArgumentsException("invalid sub");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty() || !EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new UserInvalidArgumentsException("invalid email");
        }
        checkPhoneNumber(user.getPhoneNumber());

        return user;
    }

    public User updateUser(User user, UpdateUser updateUser) {
        String phoneNumber = updateUser.getPhoneNumber();
        checkPhoneNumber(phoneNumber);
        user.setPhoneNumber(phoneNumber);

        return saveUser(user);
    }

    private boolean phoneNumberIsPossible(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, null);
            return phoneNumberUtil.isPossibleNumber(parsedNumber) && phoneNumberUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    private void checkPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumberIsPossible(phoneNumber)) {
            throw new UserInvalidArgumentsException("invalid phone number");
        }
    }
}
