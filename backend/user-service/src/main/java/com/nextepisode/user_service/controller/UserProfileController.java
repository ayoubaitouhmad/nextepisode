package com.nextepisode.user_service.controller;

import com.nextepisode.user_service.config.ApiPaths;
import com.nextepisode.user_service.dto.UserUpdateProfileRequest;
import com.nextepisode.user_service.dto.profile.UserUpdateNotificationSettingsRequest;
import com.nextepisode.user_service.dto.profile.UserUpdatePrivacySettingsRequest;
import com.nextepisode.user_service.entity.user.User;
import com.nextepisode.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPaths.BASE + "/me")
@Validated
public class UserProfileController {

    @Autowired
    private UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping
    public User updateProfile(
            @Valid @RequestBody UserUpdateProfileRequest request,
            @AuthenticationPrincipal String username
    ) {
        return userService.updateUser(request, username);
    }

    @GetMapping
    public User me(@Valid @AuthenticationPrincipal String username) {
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/change-privacy-settings")
    public ResponseEntity<Void> changePrivacySettings(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody UserUpdatePrivacySettingsRequest updateUserPrivacySettingsRequest
    ) {
        userService.changeUserPrivacySettings(updateUserPrivacySettingsRequest, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-notifications-settings")
    public ResponseEntity<Void> changeNotificationSettings(
            @AuthenticationPrincipal String username,
            @Valid @RequestBody UserUpdateNotificationSettingsRequest userUpdateNotificationSettingsRequest
    ) {
        userService.changeNotificationSettings(userUpdateNotificationSettingsRequest, username);
        return ResponseEntity.noContent().build();
    }

}
