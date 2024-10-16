package com.marketplace.marketplace.user;

import lombok.RequiredArgsConstructor;
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
        return userRepository.save(user);
    }
}
