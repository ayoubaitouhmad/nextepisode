package com.nextepisode.user_service.controller;

import com.nextepisode.user_service.dto.UserProfileRequest;
import com.nextepisode.user_service.entity.User;
import com.nextepisode.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("me")
@RestController
@Validated
public class UserProfileController {

    @Autowired
    private UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public User createOrUpdateUserProfile(
            @Valid @RequestBody UserProfileRequest request,
            @AuthenticationPrincipal String username
    ) {
        return userService.createOrUpdateUser(request, username);
    }

    @GetMapping
    public User me(@Valid @AuthenticationPrincipal String username) {
        return userService.getUserByUsername(username);
    }

}
