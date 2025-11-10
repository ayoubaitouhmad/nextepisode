package com.nextepisode.auth_service.service;

import com.nextepisode.auth_service.dto.AuthResponse;
import com.nextepisode.auth_service.dto.LoginRequest;
import com.nextepisode.auth_service.dto.RegisterRequest;
import com.nextepisode.auth_service.entity.User;
import com.nextepisode.auth_service.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());
        userRepository.save(u);
        String token = jwtService.generateToken(u.getUsername());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                // Change this line
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials"));

        // Change this line
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid credentials");
        }

        String token = jwtService.generateToken(u.getUsername());
        return new AuthResponse(token);
    }
}