package com.example.testservice.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("")
    public String hello(@AuthenticationPrincipal Object principal) {
        return "Hello from test-service! Authenticated user: " + principal;
    }
}

