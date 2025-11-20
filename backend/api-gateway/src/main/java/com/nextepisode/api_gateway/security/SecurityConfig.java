package com.nextepisode.api_gateway.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
     * Adapt this list to match your public endpoints
     * (auth service, docs, health, etc.).
     */
    private static final String[] PUBLIC_PATHS = {"/actuator/health", "/actuator/info", "/api/v1/auth/**"};

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager, JwtServerAuthenticationConverter authenticationConverter, JwtAuthFailureHandler failureHandler) {

        AuthenticationWebFilter jwtWebFilter = new AuthenticationWebFilter(authenticationManager);
        jwtWebFilter.setServerAuthenticationConverter(authenticationConverter);
        jwtWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        // ✅ IMPORTANT: this is what makes gateway return your JSON 401
        jwtWebFilter.setAuthenticationFailureHandler(failureHandler);

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable).formLogin(ServerHttpSecurity.FormLoginSpec::disable).securityContextRepository(NoOpServerSecurityContextRepository.getInstance()).authorizeExchange(ex -> ex.pathMatchers(PUBLIC_PATHS).permitAll().anyExchange().authenticated()).addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).build();
    }


    /**
     * Useful if you validate passwords somewhere.
     * Not strictly required in the gateway layer, but handy.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
