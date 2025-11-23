package com.nextepisode.auth_service.service;

import com.nextepisode.auth_service.dto.LoginRequest;
import com.nextepisode.auth_service.dto.LoginResponse;
import com.nextepisode.auth_service.dto.RegisterRequest;
import com.nextepisode.auth_service.dto.UserResponse;
import com.nextepisode.auth_service.entity.User;
import com.nextepisode.auth_service.exception.AuthenticationException;
import com.nextepisode.auth_service.exception.ErrorCode;
import com.nextepisode.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
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
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials");
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
}