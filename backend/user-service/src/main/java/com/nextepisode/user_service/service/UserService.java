package com.nextepisode.user_service.service;

import com.nextepisode.user_service.dto.UserUpdateProfileRequest;
import com.nextepisode.user_service.dto.profile.UserUpdateNotificationSettingsRequest;
import com.nextepisode.user_service.dto.profile.UserUpdatePrivacySettingsRequest;
import com.nextepisode.user_service.entity.user.User;
import com.nextepisode.user_service.exception.codes.BusinessValidationCodes;
import com.nextepisode.user_service.exception.codes.ValidationCodes;
import com.nextepisode.user_service.exception.exceptions.BusinessValidationException;
import com.nextepisode.user_service.exception.exceptions.ResourceNotFoundException;
import com.nextepisode.user_service.exception.exceptions.ValidationException;
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
        log.debug("Fetching user by username: {}", username);
        return repo.findByUsername(username);
    }

    /**
     * Get user by username (throws exception if not found)
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        log.debug("Retrieving user by username: {}", username);
        return repo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    @Transactional()
    public User save(User user) {
        log.debug("Persisting user entity: {}", user);
        try {
            User savedUser = repo.save(user);
            log.debug("User entity persisted successfully: {}", user);
            return savedUser;
        } catch (Exception e) {
            log.error("Error persisting user entity: {}", e.getMessage(), e);
            throw new BusinessValidationException("Failed to create user", e);
        }
    }

    @Transactional()
    public User createUser(User user) {
        log.debug("Processing user creating, user:{}", user);

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new ValidationException(ValidationCodes.FIELD_REQUIRED, "username");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException(ValidationCodes.FIELD_REQUIRED, "email");
        }

        try {
            User savedUser = repo.save(user);
            log.debug("User created successfully: {}", user);
            return savedUser;
        } catch (Exception e) {
            log.error("Failed to create user:{}", e.getMessage(), e);
            throw new BusinessValidationException("Failed to create user", e);
        }
    }


    @Transactional()
    public void createUserFromRegisteredEvent(String username, String email) {
        log.debug("Processing creating new user from the registered event, username:{}, email:{}" , username, email);

        Optional<User> existingUser = findByUsername(username);
        if (existingUser.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            User savedUser = createUser(user);

            log.debug("User created successfully from the registered event, user:{}" , savedUser);
        }else {
            throw new BusinessValidationException(BusinessValidationCodes.USER_ALREADY_EXIST);
        }
    }



    @Transactional
    public User updateUserProfile(UserUpdateProfileRequest request, String username) {
        log.info("Processing profile update request for user: {}", username);

        try {
            User existingUser = this.getUserByUsername(username);

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
            existingUser.setIsDirty(true);

            User savedUser = save(existingUser);
            log.info("Profile updated successfully for user: {}", username);
            return savedUser;
        } catch (Exception e) {
            log.error("Error updating profile for user {}: {}", username, e.getMessage(), e);
            throw new BusinessValidationException("Error updating profile for user", e);
        }

    }


    @Transactional
    public void changeUserPrivacySettings(UserUpdatePrivacySettingsRequest userUpdatePrivacySettingsRequest, String username) {
        log.info("Processing privacy settings update for user: {}", username);

        User existingUser = this.getUserByUsername(username);
        try {
            existingUser.setActivitySharing(userUpdatePrivacySettingsRequest.activitySharing());
            existingUser.setProfileVisibility(userUpdatePrivacySettingsRequest.profileVisibility());
            save(existingUser);

            log.info("Privacy settings updated successfully for user: {}", username);
        } catch (Exception e) {
            log.error("Error updating privacy settings for user {}: {}", username, e.getMessage(), e);
            throw new BusinessValidationException("Error updating privacy settings for user", e);
        }

    }

    @Transactional
    public void changeNotificationSettings(UserUpdateNotificationSettingsRequest userUpdateNotificationSettingsRequest, String username) {
        log.info("Processing notification settings update for user: {}", username);
        User existingUser = this.getUserByUsername(username);
        try {
            existingUser.setNotificationsEnabled(userUpdateNotificationSettingsRequest.notificationsEnabled());
            existingUser.setPushNotifications(userUpdateNotificationSettingsRequest.pushNotifications());
            save(existingUser);

            log.info("Notification settings updated successfully for user: {}", username);
        } catch (Exception e) {
            log.error("Error updating notification settings for user {}: {}", username, e.getMessage(), e);
            throw new BusinessValidationException("Error updating notification settings for user", e);
        }

    }
}