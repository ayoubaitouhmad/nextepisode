package com.nextepisode.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 1, max = 3)
    private String username;

    @NotBlank(message = "the password is required ")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be a valid email address:()")
    private String email;
}