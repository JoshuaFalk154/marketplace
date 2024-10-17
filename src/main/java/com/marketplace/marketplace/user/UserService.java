package com.marketplace.marketplace.user;

import com.marketplace.marketplace.exceptions.UserInvalidArgumentsException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findBySub(String sub) {
        return userRepository.findBySub(sub);
    }

    public User saveUser(User user) {
        User checkedUser = checkUser(user);
        return userRepository.save(checkedUser);
    }

    public User checkUser(User user) {
        if (user.getSub() == null || user.getSub().isEmpty() || user.getSub().isBlank()) {
            throw new UserInvalidArgumentsException("invalid sub");
        }


        if (user.getEmail() == null || user.getEmail().isEmpty() || !EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new UserInvalidArgumentsException("invalid email");
        }

        return user;
    }
}
