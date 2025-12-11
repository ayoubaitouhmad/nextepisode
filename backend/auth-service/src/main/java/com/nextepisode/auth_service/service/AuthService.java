package com.nextepisode.auth_service.service;

import com.nextepisode.auth_service.dto.*;
import com.nextepisode.auth_service.entity.User;
import com.nextepisode.auth_service.exception.AuthenticationException;
import com.nextepisode.auth_service.exception.ErrorCode;
import com.nextepisode.auth_service.exception.ResourceNotFoundException;
import com.nextepisode.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new AuthenticationException(ErrorCode.USERNAME_ALREADY_EXISTS, req.getUsername());
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new AuthenticationException(ErrorCode.EMAIL_ALREADY_EXISTS, req.getEmail());
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());
        userRepository.save(u);
        String token = jwtService.generateToken(u.getUsername());

        return LoginResponse.builder()
                .token(token)
                .user(
                        UserResponse.builder()
                                .id(u.getId())
                                .email(u.getEmail())
                                .username(u.getUsername()).build()
                ).build();

    }

    public LoginResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                // Change this line
                .orElseThrow(() -> new AuthenticationException(ErrorCode.USER_NOT_FOUND, req.getUsername()));


        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new AuthenticationException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(u.getUsername());
        return LoginResponse.builder()
                .token(token)
                .user(
                        UserResponse.builder()
                                .id(u.getId())
                                .email(u.getEmail())
                                .username(u.getUsername()).build()
                ).build();
    }


    /**
     * Find user by username
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Get user by username (throws exception if not found)
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        log.debug("Getting user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }


    @Transactional
    public void changePassword(UserUpdatePasswordRequest req, String username) {
        log.debug("Updating password for current user: {}", username);
        validateNewPassword(req.newPassword(), req.confirmPassword());

        User u = getUserByUsername(username);
        if (passwordEncoder.matches(req.newPassword(), u.getPassword())) {
            throw new AuthenticationException(ErrorCode.PASSWORD_SAME_AS_CURRENT);
        }

        if (!passwordEncoder.matches(req.currentPassword(), u.getPassword())) {
            throw new AuthenticationException(ErrorCode.INVALID_CREDENTIALS);
        }
        u.setPassword(passwordEncoder.encode(req.newPassword()));
        log.debug("Password updated successfully for current user: {}", username);
    }


    private void validateNewPassword(String newPassword, String confirmPassword) {
        if (!confirmPassword.equals(newPassword)) {
            throw new AuthenticationException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}