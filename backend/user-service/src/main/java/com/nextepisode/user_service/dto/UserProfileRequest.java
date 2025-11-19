package com.nextepisode.user_service.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UserProfileRequest(
        String username,
        @NotBlank(message = "First name is required")
        String firstName,
        @NotBlank(message = "Last name is required")
        String lastName,
        @Email(message = "Email must be valid") @NotBlank(message = "Email is required")
        String email,


        @Nullable String avatar,
        @Nullable String bio,
        @Nullable String location,
        @Nullable String website,
        @Nullable String phone,
        @Nullable LocalDate dateOfBirth,
        @Nullable String preferredLanguage,
        @Nullable LocalDate timezone,
        @Nullable boolean notificationsEnabled,
        @Nullable String profileVisibility,
        @Nullable String createdAt
) {
}
