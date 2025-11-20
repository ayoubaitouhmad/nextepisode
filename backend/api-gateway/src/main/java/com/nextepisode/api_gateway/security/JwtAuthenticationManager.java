package com.nextepisode.api_gateway.security;


import com.nextepisode.api_gateway.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    public JwtAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials() != null
                ? authentication.getCredentials().toString()
                : null;

        if (!StringUtils.hasText(token)) {
            // No token => let other filters decide (will end up 401 if required)
            return Mono.empty();
        }

        return Mono.fromCallable(() -> {
            Claims claims;
            try {
                claims = jwtService.parseClaims(token);
            } catch (JwtException | IllegalArgumentException ex) {
                throw new BadCredentialsException("Invalid JWT token", ex);
            }

            if (jwtService.isTokenExpired(claims)) {
                throw new BadCredentialsException("JWT token is expired");
            }

            String username = claims.getSubject();
            if (!StringUtils.hasText(username)) {
                throw new BadCredentialsException("JWT token does not contain a subject");
            }

            // Expecting a "roles" claim: ["ROLE_USER", "ROLE_ADMIN", ...]
            List<String> roles = claims.get("roles", List.class);
            List<GrantedAuthority> authorities;
            if (roles == null || roles.isEmpty()) {
                authorities = Collections.emptyList();
            } else {
                authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }

            return new UsernamePasswordAuthenticationToken(username, token, authorities);
        });
    }
}
