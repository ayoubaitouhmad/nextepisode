package com.nextepisode.user_service.service;

import com.nextepisode.user_service.dto.UserUpdateProfileRequest;
import com.nextepisode.user_service.dto.profile.UserUpdateNotificationSettingsRequest;
import com.nextepisode.user_service.dto.profile.UserUpdatePrivacySettingsRequest;
import com.nextepisode.user_service.entity.user.User;
import com.nextepisode.user_service.exception.exceptions.BusinessValidationException;
import com.nextepisode.user_service.exception.exceptions.ResourceNotFoundException;
import com.nextepisode.user_service.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;


    /**
     * Find user by username
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return repo.findByUsername(username);
    }

    /**
     * Get user by username (throws exception if not found)
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        log.debug("Getting user by username: {}", username);
        return repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }


    @Transactional()
    public User createUser(UserUpdateProfileRequest request, String username) {
        log.info("Creating new user with username: {}", username);

        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(request.email());
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setAvatar(request.avatar());
            user.setBio(request.bio());
            user.setLocation(request.location());
            user.setWebsite(request.website());
            user.setPhone(request.phone());
            user.setDateOfBirth(request.dateOfBirth());
            user.setPreferredLanguage(request.preferredLanguage());
//        user.setTimezone(request.timezone());
            log.info("Successfully created user with username: {}", username);
            return repo.save(user);

        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage(), e);
            throw new BusinessValidationException("Cannot cancel order", "Order has already shipped");

        }


    }

    @Transactional
    public User updateUser(UserUpdateProfileRequest request, String username) {
        log.info("Updating user with username: {}", username);
        User existingUser = this.getUserByUsername(username);
        try {

            existingUser.setEmail(request.email());
            existingUser.setFirstName(request.firstName());
            existingUser.setLastName(request.lastName());
            existingUser.setAvatar(request.avatar());
            existingUser.setBio(request.bio());
            existingUser.setLocation(request.location());
            existingUser.setWebsite(request.website());
            existingUser.setPhone(request.phone());
            existingUser.setDateOfBirth(request.dateOfBirth());
            existingUser.setPreferredLanguage(request.preferredLanguage());
//          existingUser.setTimezone(request.timezone());
//            existingUser.setNotificationsEnabled(request.notificationsEnabled());
            existingUser.setIsDirty(true);
            return repo.save(existingUser);
        } catch (Exception e) {
            log.error("Failed to update user: {}", e.getMessage(), e);
            throw new BusinessValidationException("Cannot cancel order", "Order has already shipped");
        }

    }


    @Transactional
    public User createOrUpdateUser(UserUpdateProfileRequest request, String username) {
        log.info("Creating or updating user with username: {}", username);

        Optional<User> existingUser = repo.findByUsername(username);

        if (existingUser.isPresent()) {
            return updateUser(request, username);
        } else {
            return createUser(request, username);
        }
    }


    @Transactional
    public void changeUserPrivacySettings(UserUpdatePrivacySettingsRequest userUpdatePrivacySettingsRequest, String username) {
        log.info("Changing user privacy settings for user: {}", username);
        User existingUser = this.getUserByUsername(username);
        try {
            existingUser.setActivitySharing(userUpdatePrivacySettingsRequest.activitySharing());
            existingUser.setProfileVisibility(userUpdatePrivacySettingsRequest.profileVisibility());
            repo.save(existingUser);
            log.info("Successfully changed user privacy settings for user: {}", username);

        } catch (Exception e) {
            log.error("Failed to change user privacy settings for user: {}", username, e);
            throw new BusinessValidationException("Failed to change user privacy settings for user", "");
        }

    }

    @Transactional
    public void changeNotificationSettings(UserUpdateNotificationSettingsRequest userUpdateNotificationSettingsRequest, String username) {
        log.info("Changing notification settings for user: {}", username);
        User existingUser = this.getUserByUsername(username);
        try {
            existingUser.setNotificationsEnabled(userUpdateNotificationSettingsRequest.notificationsEnabled());
            existingUser.setPushNotifications(userUpdateNotificationSettingsRequest.pushNotifications());
            repo.save(existingUser);
            log.info("Successfully changed notification settings for user: {}", username);
        } catch (Exception e) {
            log.error("Failed to change notification settings for user: {}", username, e);
            throw new BusinessValidationException("Failed to change user notification settings for user", "");
        }

    }
}
