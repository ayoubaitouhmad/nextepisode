package com.nextepisode.user_service.controller;

import com.nextepisode.user_service.entity.User;
import com.nextepisode.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String hello(@AuthenticationPrincipal Object principal) {
        this.userService.create();
        return "Hello from test-service! Authenticated user: " + principal;
    }

    @GetMapping("/me")
    public Optional<User> me(@RequestParam(value = "id") Long id) {
        return this.userService.findById(id);
    }
}

