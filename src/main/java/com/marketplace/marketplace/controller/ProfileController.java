package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.DTO.UpdateUser;
import com.marketplace.marketplace.DTO.UserRequested;
import com.marketplace.marketplace.DTO.UserResponse;
import com.marketplace.marketplace.user.User;
import com.marketplace.marketplace.user.UserService;
import com.marketplace.marketplace.utils.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@AuthenticationPrincipal User user, @RequestBody UpdateUser updateUser) {
        User updatedUser = userService.updateUser(user, updateUser);
        UserResponse userResponse = mapper.userToUserResponse(user);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

}
