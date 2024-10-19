package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.DTO.UserRequested;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.user.UserService;
import com.marketplace.marketplace.utils.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final Mapper mapper;

    @GetMapping
    public ResponseEntity<UserRequested> getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.loadUserEntities(user);
        UserRequested userRequested = mapper.userToUserRequested(user);
        return new ResponseEntity<>(userRequested, HttpStatus.OK);
    }

}
