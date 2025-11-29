package com.nextepisode.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Public endpoints that don't require authentication
     */
    private static final String[] PUBLIC_PATHS = {
            "/actuator/health",
            "/actuator/info",
            "/api/v1/auth/**"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            ReactiveAuthenticationManager authenticationManager,
            JwtServerAuthenticationConverter authenticationConverter,
            JwtAuthFailureHandler failureHandler) {

        // Configure JWT authentication filter
        AuthenticationWebFilter jwtWebFilter = new AuthenticationWebFilter(authenticationManager);
        jwtWebFilter.setServerAuthenticationConverter(authenticationConverter);
        jwtWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        jwtWebFilter.setAuthenticationFailureHandler(failureHandler);

        return http
                // Disable CSRF for stateless API
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Disable default authentication methods
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // Use stateless security context
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                // Configure authorization rules
                .authorizeExchange(exchanges -> exchanges
                        // Allow all OPTIONS requests for CORS preflight
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Allow public paths without authentication
                        .pathMatchers(PUBLIC_PATHS).permitAll()

                        // All other requests require authentication
                        .anyExchange().authenticated()
                )

                // Add JWT filter
                .addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)

                .build();
    }

    /**
     * Password encoder for validating passwords
     * Uses BCrypt by default with delegating support for other encoders
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}