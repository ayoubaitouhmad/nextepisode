package com.nextepisode.auth_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdatePasswordRequest(
        @NotBlank(message = "Current password is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String newPassword,

        @NotBlank(message = "Confirm password is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String confirmPassword
) {
}
