package com.nextepisode.user_service.dto.profile;

import jakarta.validation.constraints.NotNull;

public record UserUpdatePrivacySettingsRequest(
        @NotNull(message = "Profile visibility is required")
        Boolean profileVisibility,
        @NotNull(message = "Activity sharing is required")
        Boolean activitySharing
) {
}

