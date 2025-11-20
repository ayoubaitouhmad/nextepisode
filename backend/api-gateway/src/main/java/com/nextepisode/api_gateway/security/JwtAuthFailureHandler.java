package com.nextepisode.api_gateway.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

@Component
public class JwtAuthFailureHandler implements ServerAuthenticationFailureHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange,
                                              AuthenticationException ex) {

        ServerWebExchange exchange = webFilterExchange.getExchange();
        var response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String msg = ex.getMessage() == null ? "Unauthorized" : ex.getMessage();

        String error = "Unauthorized";
        String message = msg;

        // Optional mapping to your custom text
        if (msg.toLowerCase().contains("expired")) {
            error = "Token Expired";
            message = "Your authentication token has expired. Please log in again.";
        }

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", 401,
                "error", error,
                "message", message,
                "path", exchange.getRequest().getPath().value()
        );

        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(body);
        } catch (Exception e) {
            bytes = ("{\"error\":\"" + error + "\"}")
                    .getBytes(StandardCharsets.UTF_8);
        }

        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
