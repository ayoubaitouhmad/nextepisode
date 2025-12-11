package com.nextepisode.user_service.dto.profile;

import jakarta.validation.constraints.NotNull;

public record UserUpdateNotificationSettingsRequest(
        @NotNull(message = "Notifications enabled is required")
        Boolean notificationsEnabled,
        @NotNull(message = "Push notifications is required")
        Boolean pushNotifications
) {
}

